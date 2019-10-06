package com.github.vansaurabh.grpc.greeting.service;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    /**
     *
     * @param request gets GreetRequest
     * @param responseObserver
     * This method processes the request and creates appropriate response. Then sets the response to
     * Stream Observer that sends the response directly to client.
     */
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver){
        //extract the fields from request for generating response
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String result = "Hello "+ firstName;

        //Create response from the generated results
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //send the response to client by using response observer
        responseObserver.onNext(response);

        //complete the rpc call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        try {
            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstName + " responseNumber =" + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        return new StreamObserver<LongGreetRequest>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                // client sends a message
                result += "Hello " + value.getGreeting().getFirstName() + "! ";
            }

            @Override
            public void onError(Throwable t) {
                // client sends an error
            }

            @Override
            public void onCompleted() {
                // client is done
                responseObserver.onNext(
                        LongGreetResponse.newBuilder()
                                .setResult(result)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        return new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                String result = "Hello "+value.getGreeting().getFirstName();
                GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(greetEveryoneResponse);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}


