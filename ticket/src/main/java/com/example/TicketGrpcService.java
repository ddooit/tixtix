package com.example;

import com.example.ticket.TicketingGrpc;
import com.example.ticket.TicketingRequest;
import com.example.ticket.TicketingResponse;
import com.example.ticket.TicketingStatus;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketGrpcService extends TicketingGrpc.TicketingImplBase {

    private final static Logger logger = LoggerFactory.getLogger(TicketGrpcService.class);

    @Override
    public void ticketing(final TicketingRequest request, final StreamObserver<TicketingResponse> responseObserver) {
        logger.info("<<< TICKET SERVER >>> ticketing request : {}", request);

        // todo. something to ticket

        final var response = TicketingResponse.newBuilder()
                .setStatus(TicketingStatus.PAYMENT_PROCESSING)
                .setMessage("Ticketing Successful!")
                .setMemberId(request.getMemberId())
                .setTicketPrice(13_000)
                .build();

        // 응답 전달
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
