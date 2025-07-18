package com.example;

import com.example.ticket.BulkTicketingRequest;
import com.example.ticket.BulkTicketingResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkTicketGrpcService {

    private static final Logger logger = LoggerFactory.getLogger(BulkTicketGrpcService.class);

    public static StreamObserver<BulkTicketingRequest> bulkTicketing(final StreamObserver<BulkTicketingResponse> responseObserver) {

        return new StreamObserver<>() {
            int totalCount = 0;
            long totalPrice = 0;

            @Override
            public void onNext(final BulkTicketingRequest value) {
                totalCount++;
                totalPrice += 180_000;

                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("<<< TICKET SERVER >>> bulk ticketing {} 번째 request : {}", totalCount, value);
            }

            @Override
            public void onError(final Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(BulkTicketingResponse.newBuilder()
                        .setTicketCount(totalCount)
                        .setTotalPrice(totalPrice)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }
}
