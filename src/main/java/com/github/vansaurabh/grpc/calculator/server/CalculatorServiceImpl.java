package com.github.vansaurabh.grpc.calculator.server;

import com.proto.calculator.Calculator;
import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    public void calculate(CalculatorRequest request, StreamObserver<CalculatorResponse> responseStreamObserver){
        Calculator calculator = request.getCalculator();
        int firstNumber = calculator.getFirstNumber();
        int secondNumber = calculator.getSecondNumber();
        int result = firstNumber+secondNumber;

        CalculatorResponse response = CalculatorResponse.newBuilder().setResult(result).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();

    }
}
