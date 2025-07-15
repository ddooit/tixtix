package com.example.caller;

import com.example.payment.Empty;
import com.example.payment.PaymentReadyRequest;
import com.example.payment.PaymentServiceGrpc;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentClientCaller {

    private static final Logger logger = LoggerFactory.getLogger(PaymentClientCaller.class);

    private final PaymentServiceGrpc.PaymentServiceFutureStub futureStub;

    PaymentClientCaller(final ManagedChannel channel) {
        this.futureStub = PaymentServiceGrpc.newFutureStub(channel);
    }

    public ListenableFuture<Empty> callPaymentGetReady(final PaymentReadyRequest request) {
        logger.info("<<< PAYMENT CLIENT >>> get ready : {}", request);
        
        return futureStub.getReady(request);
    }

}
