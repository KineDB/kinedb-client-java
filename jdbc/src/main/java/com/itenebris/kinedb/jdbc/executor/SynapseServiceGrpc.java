package com.itenebris.kinedb.jdbc.executor;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.52.0)",
    comments = "Source: kine.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SynapseServiceGrpc {

  private SynapseServiceGrpc() {}

  public static final String SERVICE_NAME = "proto.SynapseService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Kine.Statement,
      Kine.Results> getExecuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "execute",
      requestType = Kine.Statement.class,
      responseType = Kine.Results.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Kine.Statement,
      Kine.Results> getExecuteMethod() {
    io.grpc.MethodDescriptor<Kine.Statement, Kine.Results> getExecuteMethod;
    if ((getExecuteMethod = SynapseServiceGrpc.getExecuteMethod) == null) {
      synchronized (SynapseServiceGrpc.class) {
        if ((getExecuteMethod = SynapseServiceGrpc.getExecuteMethod) == null) {
          SynapseServiceGrpc.getExecuteMethod = getExecuteMethod =
              io.grpc.MethodDescriptor.<Kine.Statement, Kine.Results>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "execute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Kine.Statement.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Kine.Results.getDefaultInstance()))
              .setSchemaDescriptor(new SynapseServiceMethodDescriptorSupplier("execute"))
              .build();
        }
      }
    }
    return getExecuteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SynapseServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynapseServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynapseServiceStub>() {
        @Override
        public SynapseServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynapseServiceStub(channel, callOptions);
        }
      };
    return SynapseServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SynapseServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynapseServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynapseServiceBlockingStub>() {
        @Override
        public SynapseServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynapseServiceBlockingStub(channel, callOptions);
        }
      };
    return SynapseServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SynapseServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynapseServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynapseServiceFutureStub>() {
        @Override
        public SynapseServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynapseServiceFutureStub(channel, callOptions);
        }
      };
    return SynapseServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class SynapseServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void execute(Kine.Statement request,
                        io.grpc.stub.StreamObserver<Kine.Results> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExecuteMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getExecuteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                Kine.Statement,
                Kine.Results>(
                  this, METHODID_EXECUTE)))
          .build();
    }
  }

  /**
   */
  public static final class SynapseServiceStub extends io.grpc.stub.AbstractAsyncStub<SynapseServiceStub> {
    private SynapseServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SynapseServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynapseServiceStub(channel, callOptions);
    }

    /**
     */
    public void execute(Kine.Statement request,
                        io.grpc.stub.StreamObserver<Kine.Results> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SynapseServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<SynapseServiceBlockingStub> {
    private SynapseServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SynapseServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynapseServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public Kine.Results execute(Kine.Statement request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExecuteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SynapseServiceFutureStub extends io.grpc.stub.AbstractFutureStub<SynapseServiceFutureStub> {
    private SynapseServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected SynapseServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynapseServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Kine.Results> execute(
        Kine.Statement request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_EXECUTE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SynapseServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SynapseServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EXECUTE:
          serviceImpl.execute((Kine.Statement) request,
              (io.grpc.stub.StreamObserver<Kine.Results>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SynapseServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SynapseServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Kine.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SynapseService");
    }
  }

  private static final class SynapseServiceFileDescriptorSupplier
      extends SynapseServiceBaseDescriptorSupplier {
    SynapseServiceFileDescriptorSupplier() {}
  }

  private static final class SynapseServiceMethodDescriptorSupplier
      extends SynapseServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SynapseServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SynapseServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SynapseServiceFileDescriptorSupplier())
              .addMethod(getExecuteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
