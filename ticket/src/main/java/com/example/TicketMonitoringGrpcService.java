package com.example;

import com.example.ticket.TicketMonitoringRequest;
import com.example.ticket.TicketMonitoringResponse;
import com.example.ticket.TicketingGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketMonitoringGrpcService extends TicketingGrpc.TicketingImplBase {

    private final static Logger logger = LoggerFactory.getLogger(TicketMonitoringGrpcService.class);

    @Override
    public void ticketMonitoring(final TicketMonitoringRequest request, final StreamObserver<TicketMonitoringResponse> responseObserver) {
        logger.info("<<< TICKET SERVER >>> ticket monitoring request : {}", request);

        final var first = TicketMonitoringResponse.newBuilder()
                .setRemainingTicketCount(10_000)
                .build();
        responseObserver.onNext(first);

        try {
            Thread.sleep(1_000);
            final var second = TicketMonitoringResponse.newBuilder()
                    .setRemainingTicketCount(9_000)
                    .build();
            responseObserver.onNext(second);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(2_000);
            final var third = TicketMonitoringResponse.newBuilder()
                    .setRemainingTicketCount(7_000)
                    .build();
            responseObserver.onNext(third);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final var fourth = TicketMonitoringResponse.newBuilder()
                .setRemainingTicketCount(500)
                .build();
        responseObserver.onNext(fourth);

        responseObserver.onCompleted();
    }

}
