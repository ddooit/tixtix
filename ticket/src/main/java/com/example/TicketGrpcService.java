package com.example;

import com.example.ticket.*;
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

    @Override
    public void ticketMonitoring(TicketMonitoringRequest request, StreamObserver<TicketMonitoringResponse> responseObserver) {
        TicketMonitoringGrpcService.ticketMonitoring(request, responseObserver);
    }

    @Override
    public StreamObserver<BulkTicketingRequest> bulkTicketing(StreamObserver<BulkTicketingResponse> responseObserver) {
        return BulkTicketGrpcService.bulkTicketing(responseObserver);
    }

    @Override
    public StreamObserver<SoldOutTicketingRequest> soldOutTicketing(StreamObserver<SoldOutTicketingResponse> responseObserver) {
        return SoldOutTicketGrpcService.soldOutTicketing(responseObserver);
    }
}
