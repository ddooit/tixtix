package com.example.caller;

import com.example.dto.TicketingRequest;
import com.example.ticket.TicketMonitoringRequest;
import com.example.ticket.TicketMonitoringResponse;
import com.example.ticket.TicketingGrpc;
import com.example.ticket.TicketingResponse;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class TicketClientCaller {

    private static final Logger logger = LoggerFactory.getLogger(TicketClientCaller.class);

    private final TicketingGrpc.TicketingBlockingStub blockingStub;

    TicketClientCaller(final ManagedChannel channel) {
        this.blockingStub = TicketingGrpc.newBlockingStub(channel);
    }

    public TicketingResponse callTicketingRequest(final TicketingRequest request) {
        logger.info("<<< TICKET CLIENT >>> ticketing request : {}", request);

        return blockingStub.ticketing(request.toGrpcRequest());
    }

    public Iterator<TicketMonitoringResponse> callMonitoring(final long performanceId) {
        logger.info("<<< TICKET CLIENT >>> call monitoring for : {}", performanceId);

        return blockingStub.ticketMonitoring(TicketMonitoringRequest.newBuilder()
                        .setPerformanceId(performanceId)
                        .build());
    }

}
