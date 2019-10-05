package com.github.vansaurabh.grpc.server;

import com.github.vansaurabh.grpc.calculator.server.CalculatorServiceImpl;
import com.github.vansaurabh.grpc.greeting.server.GreetServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC");

        Server server = ServerBuilder.forPort(50051)
                .addService(new CalculatorServiceImpl())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received ShutDown Request");
            server.shutdown();
            System.out.println("Server stopped successfully!!");
        }));
        server.awaitTermination();
    }
}
