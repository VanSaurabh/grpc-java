package com.github.vansaurabh.grpc.calculator.service;

import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
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
}
