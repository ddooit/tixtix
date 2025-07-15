package com.example;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class PaymentGrpcServer {
    private static final int PAYMENT_SERVER_PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {

        final var ticketServer = ServerBuilder.forPort(PAYMENT_SERVER_PORT)
                .addService(new PaymentGrpcService())
                .build();

        ticketServer.start();
        ticketServer.awaitTermination();
    }
}
