package com.github.vansaurabh.grpc.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public static void main(String[] args) {
        CalculatorClient calculatorClient = new CalculatorClient();
        calculatorClient.run();
    }

    private void run(){
        System.out.println("Hello This is gRPC calculator client");
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50052).usePlaintext()
                  .build();
        doUnaryCall(channel);
        doClientStreamingCall(channel);
        doServerStreamingCall(channel);
        doBiDirectionalStreamingCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doBiDirectionalStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<CalculateMaximumRequest>  requestStreamObserver = asyncClient.calculateMaximum(new StreamObserver<CalculateMaximumResponse>() {
            @Override
            public void onNext(CalculateMaximumResponse value) {
                System.out.println("Response received from server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("client finished sending data");
                latch.countDown();
            }
        });

        System.out.println("Sending 1st Request");
        requestStreamObserver.onNext(CalculateMaximumRequest.newBuilder()
                .setInputNumber(4)
                .build());
        System.out.println("Sending 1st Request");
        requestStreamObserver.onNext(CalculateMaximumRequest.newBuilder()
                .setInputNumber(3)
                .build());

        System.out.println("Sending 1st Request");
        requestStreamObserver.onNext(CalculateMaximumRequest.newBuilder()
                .setInputNumber(2)
                .build());

        System.out.println("Sending 1st Request");
        requestStreamObserver.onNext(CalculateMaximumRequest.newBuilder()
                .setInputNumber(5)
                .build());

        requestStreamObserver.onCompleted();
        try{
            latch.await(3L, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void doServerStreamingCall(ManagedChannel channel) {
        //Streaming server
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        int number = 12;
        stub.calculatePrimeNumber(CalculatePrimeNumberRequest.newBuilder().setInputNumber(number).build())
                .forEachRemaining(calculatePrimeNumberResponse -> System.out.println(calculatePrimeNumberResponse.getResult()));
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncStub = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<CalculateAverageRequest> requestStreamObserver = asyncStub.calculateAverage(new StreamObserver<CalculateAverageResponse>() {
            @Override
            public void onNext(CalculateAverageResponse value) {
                System.out.println("Got a response from server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending the request");
                latch.countDown();
            }
        });
        // streaming message #1
        System.out.println("sending message 1");
        requestStreamObserver.onNext(CalculateAverageRequest.newBuilder()
                .setInputNumber(2)
                .build());

        // streaming message #2
        System.out.println("sending message 2");
        requestStreamObserver.onNext(CalculateAverageRequest.newBuilder()
                .setInputNumber(5)
                .build());

        // streaming message #3
        System.out.println("sending message 3");
        requestStreamObserver.onNext(CalculateAverageRequest.newBuilder()
                .setInputNumber(4)
                .build());

        // we tell the server that the client is done sending data
        requestStreamObserver.onCompleted();

        try{
            latch.await(3L, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void doUnaryCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        CalculatorRequest request = CalculatorRequest.newBuilder().setFirstNumber(3).setSecondNumber(10).build();
        CalculatorResponse response = stub.calculator(request);
        System.out.println(request.getFirstNumber() +" + "+ request.getSecondNumber() +" = " + response.getResult());
    }
}
