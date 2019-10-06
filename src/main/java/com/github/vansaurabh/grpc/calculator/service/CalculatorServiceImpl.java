package com.github.vansaurabh.grpc.calculator.service;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void calculator(CalculatorRequest request, StreamObserver<CalculatorResponse> responseStreamObserver){
        int firstNumber = request.getFirstNumber();
        int secondNumber = request.getSecondNumber();
        int result = firstNumber+secondNumber;

        CalculatorResponse response = CalculatorResponse.newBuilder().setResult(result).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void calculatePrimeNumber(CalculatePrimeNumberRequest request, StreamObserver<CalculatePrimeNumberResponse> responseObserver) {
        int number = request.getInputNumber();
        calculatePrimeNumber(number, responseObserver);
    }

    private void calculatePrimeNumber(int number, StreamObserver<CalculatePrimeNumberResponse> responseObserver){
        int k= 2;
        while(number > 1){
            if(number % k == 0) {
                number = number/2;
                responseObserver.onNext(CalculatePrimeNumberResponse.newBuilder()
                        .setResult(k)
                        .build());
            }else{
                k += 1;
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CalculateAverageRequest> calculateAverage(StreamObserver<CalculateAverageResponse> responseObserver) {
        return new StreamObserver<CalculateAverageRequest>() {
            int counter = 0;
            int sumValue = 0;
            @Override
            public void onNext(CalculateAverageRequest value) {
                sumValue += value.getInputNumber();
                counter++;
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                double average = (double) sumValue/counter;
                responseObserver.onNext(CalculateAverageResponse.newBuilder().setResult(average).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<CalculateMaximumRequest> calculateMaximum(StreamObserver<CalculateMaximumResponse> responseObserver) {
        return new StreamObserver<CalculateMaximumRequest>() {
            int max = -1;
            @Override
            public void onNext(CalculateMaximumRequest value) {
                if(value.getInputNumber() > max)
                    max = value.getInputNumber();
                CalculateMaximumResponse response = CalculateMaximumResponse.newBuilder()
                        .setResult(max)
                        .build();
                responseObserver.onNext(response);
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
