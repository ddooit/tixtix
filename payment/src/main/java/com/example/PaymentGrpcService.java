package com.example;

import com.example.payment.Empty;
import com.example.payment.PaymentReadyRequest;
import com.example.payment.PaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(PaymentGrpcService.class);

    @Override
    public void getReady(final PaymentReadyRequest request, final StreamObserver<Empty> responseObserver) {
        logger.info("<<< PAYMENT SERVER >>> getReady request : {}", request);

        // todo. something to payment

        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("<<< PAYMENT SERVER >>> 3 초 끝 !!");

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
