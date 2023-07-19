package com.itenebris.kinedb.jdbc.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.result.ResultData;
import com.itenebris.kinedb.jdbc.util.KineTypeUtils;
import com.itenebris.kinedb.jdbc.util.StringUtils;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KineDBGrpcClient {

    static Logger log = LoggerFactory.getLogger(KineDBGrpcClient.class);

    ManagedChannel channel;
    public SynapseServiceGrpc.SynapseServiceBlockingStub blockingStub;

    public SynapseServiceGrpc.SynapseServiceStub asyncStub;

    final ThreadPoolExecutor grpcExecutor ;

    String engine;
    String database;

    int fetchSize;

    public KineDBGrpcClient(String ip, int port, String database, String engine, int fetchSize) {
        grpcExecutor = new ThreadPoolExecutor(Runtime.getRuntime()
                .availableProcessors() * 8,
                Runtime.getRuntime().availableProcessors() * 8, 1L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("kineDB-grpc-client-executor-%d")
                        .build());
        this.database = database;
        this.engine = engine;
        this.fetchSize = fetchSize;
        connectToServer(ip, port, database, engine);
    }

    public void connectToServer(String serverIp, int serverPort, String database, String engine){
//        ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forAddress(serverIp, serverPort)
//                .executor(grpcExecutor)
//                .compressorRegistry(CompressorRegistry.getDefaultInstance())
//                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
//                .maxInboundMessageSize(1024 * 1024 * 1024)
//                .keepAliveTime(6 * 60 * 1000, TimeUnit.MILLISECONDS)
//                .usePlaintext();
        ManagedChannelBuilder builder = ManagedChannelBuilder.forAddress(serverIp, serverPort).
                maxInboundMessageSize(1024 * 1024 * 1024).
                usePlaintext();

        ClientInterceptor initInterceptor = new GrpcClientInterceptor(database, engine);

        this.channel = builder.intercept(initInterceptor).build();
        this.blockingStub = SynapseServiceGrpc.newBlockingStub(channel);
        this.asyncStub = SynapseServiceGrpc.newStub(channel);
        log.debug("KineDBGrpcClient connectToServer serverIp:" + serverIp + " serverPort:" + serverPort +" success");
    }

    public class GrpcClientInterceptor implements ClientInterceptor {

        String engine;
        String database;
        String sessionId;

        String initConnection = "true";

        final Metadata.Key<String> ENGINE_KEY = Metadata.Key.of("engine", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata.Key<String> DATA_BASE_KEY = Metadata.Key.of("database", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata.Key<String> SESSION_ID_KEY = Metadata.Key.of("session-id", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata.Key<String> INIT_CONNECTION_KEY = Metadata.Key.of("init-connection", Metadata.ASCII_STRING_MARSHALLER);

        public GrpcClientInterceptor(String database, String engine) {
            this.database = database;
            this.engine = engine;
        }

        @Override
        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    if (StringUtils.isNullOrEmpty(sessionId) && !StringUtils.isNullOrEmpty(database)) {
                        // add default database
                        headers.put(DATA_BASE_KEY, database);
                        log.info("InitClientInterceptor with database {}", database);
                    }
                    if (StringUtils.isNullOrEmpty(sessionId) && !StringUtils.isNullOrEmpty(engine)) {
                        // add default engine
                        headers.put(ENGINE_KEY, engine);
                        log.info("InitClientInterceptor with engine {}", engine);
                    }
                    if (StringUtils.nullSafeEqual(initConnection, "true")) {
                        // for first connection
                        headers.put(INIT_CONNECTION_KEY, initConnection);
                        initConnection = "false";
                        log.info("InitClientInterceptor with initConnection {}", initConnection);
                    }
                    if (!StringUtils.isNullOrEmpty(sessionId)) {
                        // add sessionId
                        headers.put(SESSION_ID_KEY, sessionId);
                        log.debug("InitClientInterceptor with sessionId {}", sessionId);
                    }

                    super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                        @Override
                        public void onHeaders(Metadata headers) {
                            //return headers

                            String returnSessionId = headers.get(SESSION_ID_KEY);
                            //log.info("InitClientInterceptor header received from server {}", headers);
                            log.info("InitClientInterceptor get sessionId from server " + returnSessionId);
                            sessionId = returnSessionId;
                            super.onHeaders(headers);
                        }
                    }, headers);
                }
            };
        }

    }


    public ResultData executeSql(String sql, ExecutorParams executorParams) throws Exception {
        long start = System.nanoTime();
        Kine.Statement request = buildRequest(sql, executorParams);
        Kine.Results results = this.blockingStub.execute(request);

        log.info("kineDBGrpc executeSql response code:{}, message:{}, consume:{}",results.getCode(), results.getMessage(), (System.nanoTime()-start)/1000000);
        if (results.getCode() != 200) {
            throw new SQLException(results.getMessage());
        }

        KineData result = KineData.convertExecuteSqlResult(results);
        result.buildIndexMapping();
        long end = System.nanoTime();
        long consume = (end - start) / 1000000;
        log.info("kinedb client executeSql consume [{}]", consume);
        return result;
    }

    public Iterator<Kine.Results> streamExecuteSql(String sql, ExecutorParams executorParams) {

        Kine.Statement request = buildRequest(sql, executorParams);
        Iterator<Kine.Results> resultsIterator = this.blockingStub.streamExecute(request);
        log.info("streamExecuteSql build resultsIterator success for sql [{}]", sql);
        return resultsIterator;
    }

    private Kine.Statement buildRequest(String sql, ExecutorParams executorParams) {
        Kine.Statement.Builder builder = Kine.Statement.newBuilder();
        builder.setSql(sql);
        String defaultDatabase = executorParams.getDefaultDatabase();
        if (StringUtils.isBlank(defaultDatabase)) {
            defaultDatabase = this.database;
        }
        String engine = executorParams.getEngine();
        if (StringUtils.isBlank(engine)) {
            engine = this.engine;
        }
        int fetchSize = executorParams.getFetchSize();
        if (fetchSize <= 0) {
            fetchSize = this.fetchSize;
        }
        if (StringUtils.isNotBlank(defaultDatabase)) {
            builder.setDefaultDatabase(defaultDatabase);
        }
        if (StringUtils.isNotBlank(engine)) {
            builder.setEngine(engine);
        }
        if (fetchSize > 0) {
            builder.setFetchSize(fetchSize);
        }
        Kine.Statement request = builder.build();
        return request;
    }

    public void streamExecute(String sql) throws SQLException {
        long start = System.nanoTime();

        try {
            Context.CancellableContext streamExecuteContext = null;

            Kine.Statement request = Kine.Statement.newBuilder().setSql(sql).build();
            StreamObserver<Kine.Results> responseObserver = new StreamObserver<Kine.Results>() {
                @Override
                public void onNext(Kine.Results value) {
                    System.out.println("streamExecute result = \n" + value);
                }
                @Override
                public void onError(Throwable t) {
                    log.info("kineDBGrpc streamExecute error message:{}", t);
                }
                @Override
                public void onCompleted() {
                    log.info("kineDBGrpc streamExecute completed");
                }
            };

            Runnable streamExecuteTask = () -> asyncStub.streamExecute(request, responseObserver);
            if (streamExecuteContext != null && !streamExecuteContext.isCancelled()) {
                log.info("kineDBGrpc streamExecute already executing");
                return;
            }
            streamExecuteContext = Context.current().withCancellation();
            streamExecuteContext.run(streamExecuteTask);
        } catch (Exception e) {
            log.error("kineDBGrpc streamExecute error", e);
            throw new SQLException(e.getMessage());
        }

        long end = System.nanoTime();
        long consume = (end - start) / 1000000;
        log.info("kineDBGrpc streamExecute consume [{}]", consume);
        //return null;
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        KineDBGrpcClient kineDBGrpcClient = new KineDBGrpcClient("127.0.0.1", 10301, "mysql_db", "native", 0);

        String sql = "select * from mysql_db.t_user where id > 0";

        try {
            ExecutorParams executorParams = new ExecutorParams(1000, "mysql_db", "native");
            kineDBGrpcClient.executeSql(sql, executorParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        String sql2 = "select * from mysql_db.t_user where id > 2";
//
//        try {
//            kineDBGrpcClient.executeSql(sql2);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}

