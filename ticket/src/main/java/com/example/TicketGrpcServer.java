package com.example;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class TicketGrpcServer {

    private static final int TICKET_SERVER_PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {

        final var ticketServer = ServerBuilder.forPort(TICKET_SERVER_PORT)
                .addService(new TicketGrpcService())
                .addService(new TicketMonitoringGrpcService())
                .addService(new BulkTicketGrpcService())
                .build();

        ticketServer.start();
        ticketServer.awaitTermination();
    }
}
