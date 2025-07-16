package com.example.caller;

import com.example.dto.TicketingRequest;
import com.example.ticket.*;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TicketClientCaller {

    private static final Logger logger = LoggerFactory.getLogger(TicketClientCaller.class);

    private final TicketingGrpc.TicketingBlockingStub blockingStub;

    private final TicketingGrpc.TicketingStub asyncStub;

    TicketClientCaller(final ManagedChannel channel) {
        this.blockingStub = TicketingGrpc.newBlockingStub(channel);
        this.asyncStub = TicketingGrpc.newStub(channel);
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

    public BulkTicketingResponse callBulkTicketingRequest(final List<BulkTicketingRequest> ticketingRequests) {
        logger.info("<<< TICKET CLIENT >>> bulk ticketing request : {}", ticketingRequests);

        CompletableFuture<BulkTicketingResponse> completableFuture = new CompletableFuture<>();

        final var responseStreamObserver = new StreamObserver<BulkTicketingResponse>() {

            @Override
            public void onNext(final BulkTicketingResponse value) {
                logger.info("<<< TICKET CLIENT >>> bulk ticketing response : {}", value);
                completableFuture.complete(value);
            }

            @Override
            public void onError(final Throwable t) {
                logger.error("<<< TICKET CLIENT >>> bulk ticketing error : {}", t.getMessage());
                completableFuture.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                logger.info("<<< TICKET CLIENT >>> bulk ticketing completed");
                if (!completableFuture.isDone()) {
                    completableFuture.complete(null); // 또는 적절한 기본값/예외 처리
                }
            }
        };

        final var requestStreamObserver = asyncStub.bulkTicketing(responseStreamObserver);

        ticketingRequests
                .forEach(request -> {
                    logger.info("<<< TICKET CLIENT >>> Sending request: {}", request);
                    requestStreamObserver.onNext(request);
                });

        logger.info("<<< TICKET CLIENT >>> Sending request DONE");
        requestStreamObserver.onCompleted(); // 서버에게 request 종료 알림.

        try {
            return completableFuture.get(1, TimeUnit.MINUTES); // 최대 1분 기다림.
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }


    public void callSoldOutTicketing(final List<SoldOutTicketingRequest> soldOutTicketingRequests) {
        logger.info("<<< TICKET CLIENT >>> sold out ticketing request : {}", soldOutTicketingRequests);

        final var responseStreamObserver = new StreamObserver<SoldOutTicketingResponse>() {
            int count = 0;

            @Override
            public void onNext(final SoldOutTicketingResponse value) {
                logger.info("<<< TICKET CLIENT >>> sold out [{}] : {}", count++, value);
            }

            @Override
            public void onError(final Throwable t) {
                logger.info("<<< TICKET CLIENT >>> sold out ticketing ERROR : {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("<<< TICKET CLIENT >>> sold out ticketing Completed");
            }
        };

        final var requestStreamObserver = asyncStub.soldOutTicketing(responseStreamObserver);


        soldOutTicketingRequests.forEach(
                soldOutTicketingRequest -> {
                    logger.info("<<< TICKET CLIENT >>> Sending Ticketing request: {}", soldOutTicketingRequest);

                    requestStreamObserver.onNext(soldOutTicketingRequest);
                });

        requestStreamObserver.onCompleted();

    }

}
