package com.github.vansaurabh.grpc.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

 import java.util.Arrays;
 import java.util.concurrent.CountDownLatch;
 import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello This is gRPC client");
        GreetingClient greetingClient;
        greetingClient = new GreetingClient();
        greetingClient.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        doUnaryCall(channel);
        doServerStreamingCall(channel);
        doClientStreamingCall(channel);
        doBiDirectionalStreamingCall(channel);

        //shutting down channel
        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doBiDirectionalStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<GreetEveryoneRequest> greetEveryoneRequestStreamObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
                System.out.println("Response from server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("user1", "user2", "user3").forEach(names -> greetEveryoneRequestStreamObserver.
                onNext(GreetEveryoneRequest
                        .newBuilder()
                        .setGreeting(Greeting
                                .newBuilder()
                                .setFirstName(names)
                                .build())
                        .build()));
        greetEveryoneRequestStreamObserver.onCompleted();

        try{
            latch.await(3L, TimeUnit.SECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // we get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
                // onNext will be called only once
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                // onCompleted will be called right after onNext()
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        // streaming message #1
        System.out.println("sending message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Stephane")
                        .build())
                .build());

        // streaming message #2
        System.out.println("sending message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("John")
                        .build())
                .build());

        // streaming message #3
        System.out.println("sending message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Marc")
                        .build())
                .build());

        // we tell the server that the client is done sending data
        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub  greetServerStreamingClient = GreetServiceGrpc.newBlockingStub(channel);
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                                                      .setGreeting(Greeting.newBuilder()
                                                      .setFirstName("Saurabh")
                                                      .setFirstName("Kundu"))
                                                      .build();
        greetServerStreamingClient.greetManyTimes(greetManyTimesRequest)
                                  .forEachRemaining(GreetManyTimesResponse::getResult);

    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder()
                            .setFirstName("Saurabh")
                            .setLastName("Kundu")
                            .build();

        GreetRequest request = GreetRequest.newBuilder().setGreeting(greeting).build();
        GreetResponse response = greetClient.greet(request);

        System.out.println(response.getResult());
    }
}
