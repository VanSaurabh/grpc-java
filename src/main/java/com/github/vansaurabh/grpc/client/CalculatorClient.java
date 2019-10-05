package com.github.vansaurabh.grpc.client;

import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        System.out.println("Hello This is gRPC calculator client");

        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051).usePlaintext()
                .build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        CalculatorRequest request = CalculatorRequest.newBuilder().setFirstNumber(3).setSecondNumber(10).build();
        CalculatorResponse response = stub.calculator(request);

        System.out.println(request.getFirstNumber() +" + "+ request.getSecondNumber() +" = " + response.getResult());

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
