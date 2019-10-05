package com.github.vansaurabh.grpc.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
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
}
