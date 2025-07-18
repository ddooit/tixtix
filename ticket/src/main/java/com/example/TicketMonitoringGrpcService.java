package com.example;

import com.example.ticket.TicketMonitoringRequest;
import com.example.ticket.TicketMonitoringResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class TicketMonitoringGrpcService {

    private final static Logger logger = LoggerFactory.getLogger(TicketMonitoringGrpcService.class);

    public static void ticketMonitoring(final TicketMonitoringRequest request, final StreamObserver<TicketMonitoringResponse> responseObserver) {
        logger.info("<<< TICKET SERVER >>> ticket monitoring request : {}", request);

        Stream.of(ticketMonitoringResponse(10_000),
                        ticketMonitoringResponse(9_000),
                        ticketMonitoringResponse(7_000),
                        ticketMonitoringResponse(500))
                .forEach(response -> {
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    responseObserver.onNext(response);
                });

        responseObserver.onCompleted();
    }

    private static TicketMonitoringResponse ticketMonitoringResponse(final int value) {
        return TicketMonitoringResponse.newBuilder()
                .setRemainingTicketCount(value)
                .build();
    }

}
