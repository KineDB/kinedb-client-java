package com.itenebris.kinedb.jdbc.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.result.ResultData;
import com.itenebris.kinedb.jdbc.util.KineTypeUtils;
import com.itenebris.kinedb.jdbc.util.StringUtils;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KineDBGrpcClient {

    Logger log = LoggerFactory.getLogger(KineDBGrpcClient.class);

    ManagedChannel channel;
    public SynapseServiceGrpc.SynapseServiceBlockingStub blockingStub;

    final ThreadPoolExecutor grpcExecutor ;

    public KineDBGrpcClient(String ip, int port, String database) {
        grpcExecutor = new ThreadPoolExecutor(Runtime.getRuntime()
                .availableProcessors() * 8,
                Runtime.getRuntime().availableProcessors() * 8, 1L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("kineDB-grpc-client-executor-%d")
                        .build());

        connectToServer(ip, port, database);
    }

    public void connectToServer(String serverIp, int serverPort, String database){
        ManagedChannelBuilder<?> o = ManagedChannelBuilder.forAddress(serverIp, serverPort)
                .executor(grpcExecutor)
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
                .maxInboundMessageSize(10 * 1024 * 1024)
                .keepAliveTime(6 * 60 * 1000, TimeUnit.MILLISECONDS)
                .usePlaintext();
        ManagedChannelBuilder builder = ManagedChannelBuilder.forAddress(serverIp, serverPort).
                maxInboundMessageSize(2147483647).
                usePlaintext();

        ClientInterceptor initInterceptor = new GrpcClientInterceptor(database);

        this.channel = builder.intercept(initInterceptor).build();
        blockingStub = SynapseServiceGrpc.newBlockingStub(channel);
        log.debug("KineDBGrpcClient connectToServer serverIp:" + serverIp + " serverPort:" + serverPort +" success");
    }

    public class GrpcClientInterceptor implements ClientInterceptor {

        String database;
        String sessionId;

        String initConnection = "true";
        final Metadata.Key<String> DATA_BASE_KEY = Metadata.Key.of("database", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata.Key<String> SESSION_ID_KEY = Metadata.Key.of("session-id", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata.Key<String> INIT_CONNECTION_KEY = Metadata.Key.of("init-connection", Metadata.ASCII_STRING_MARSHALLER);

        public GrpcClientInterceptor(String database) {
            this.database = database;
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
                            log.info("InitClientInterceptor header received from server {}", headers);
                            System.out.println("InitClientInterceptor get sessionId from server " + returnSessionId);
                            sessionId = returnSessionId;
                            super.onHeaders(headers);
                        }
                    }, headers);
                }
            };
        }

    }


    public ResultData executeSql(String sql) throws Exception {
        Kine.Statement request = Kine.Statement.newBuilder().setSql(sql).build();
        Kine.Results results = this.blockingStub.execute(request);

        System.out.println("kineDBGrpc executeSql response code :" + results.getCode() + " message : "+ results.getMessage());
        if (results.getCode() != 200) {
            throw new SQLException(results.getMessage());
        }

        KineData result = convertExecuteSqlResult(results);
        result.buildIndexMapping();
        return result;
    }

    private static KineData convertExecuteSqlResult(Kine.Results result) throws SQLException {
        System.out.println("ExecuteSQL result.Rows length " + result.getRowsCount());
        int rowCount = result.getRowsCount();

        // 1. use the first row to generate the result rowNames
        List<String> rowNames = null;
        List<String> rowTypes = null;
        if (rowCount > 0) {
            Kine.RowRef firstRow = result.getRows(0);
            rowNames = new ArrayList<>(firstRow.getColumnsCount());
            rowTypes = new ArrayList<>(firstRow.getColumnsCount());

            for (int i=0; i< firstRow.getColumnsCount(); i++) {
                Kine.ColumnValueRef column = firstRow.getColumns(i);
                rowNames.add(column.getName());
                rowTypes.add(column.getType());
            }
        }
        System.out.println("convertExecuteSqlResultRow rowNames: " + rowNames);

        // 2. generate the result rowValues
        List<Object[]> rowValues  = new ArrayList<>(rowCount);
        for (int i=0; i<rowCount; i++) {
            Object[] rowValue = convertExecuteSqlResultRow(result.getRows(i));
            rowValues.add(rowValue);
        }


        KineData resultData = new KineData();
        if (rowNames != null) {
            resultData.setRowNames(rowNames.toArray((new String[0])));
            resultData.setTypes(rowTypes.toArray((new String[0])));
            resultData.setRowValues(rowValues.toArray(new Object[0][0]));
        }

        System.out.println("ExecuteSQL result :\n " + resultData);
        return resultData;
    }

    private static Object[] convertExecuteSqlResultRow(Kine.RowRef row ) throws SQLException {

        List<Object> values = new ArrayList<>();

        for (int i=0; i<row.getColumnsCount(); i++) {
            Kine.ColumnValueRef columnValue = row.getColumns(i);
            Object colValue = convertColumnValue(columnValue);
            values.add(colValue);
        }
        System.out.println("convertExecuteSqlResultRow rowValue: " + values);
        return values.toArray();
    }

    private static Object convertColumnValue(Kine.ColumnValueRef columnValue) throws SQLException {
        KineType kineType = KineType.getByName(columnValue.getType());
        Object value = KineTypeUtils.kineTypeBytesToJavaTypes(kineType, columnValue.getValue().toByteArray());
        return value;
    }


    public static void main(String[] args) throws InvalidProtocolBufferException {
        KineDBGrpcClient kineDBGrpcClient = new KineDBGrpcClient("127.0.0.1", 10301, "mysql_db");

        String sql = "select * from mysql_db.user1 where id > 1";

        try {
            kineDBGrpcClient.executeSql(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String sql2 = "select * from mysql_db.user1 where id > 2";

        try {
            kineDBGrpcClient.executeSql(sql2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

