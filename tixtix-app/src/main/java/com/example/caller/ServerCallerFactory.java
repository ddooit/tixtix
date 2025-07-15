package com.example.caller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ServerCallerFactory {

    private static final ManagedChannel TICKET_CHANNEL = ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build();

    private static final ManagedChannel PAYMENT_CHANNEL = ManagedChannelBuilder
            .forAddress("localhost", 50052)
            .usePlaintext()
            .build();

    public static TicketClientCaller ticketClientCaller() {
        return new TicketClientCaller(TICKET_CHANNEL);
    }

    public static PaymentClientCaller paymentClientCaller() {
        return new PaymentClientCaller(PAYMENT_CHANNEL);
    }

}
