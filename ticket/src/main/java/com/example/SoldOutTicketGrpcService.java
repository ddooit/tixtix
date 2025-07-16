package com.example;

import com.example.ticket.SoldOutTicketingRequest;
import com.example.ticket.SoldOutTicketingResponse;
import com.example.ticket.TicketingGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SoldOutTicketGrpcService extends TicketingGrpc.TicketingImplBase {

    private final static Logger logger = LoggerFactory.getLogger(SoldOutTicketGrpcService.class);

    private final static List<String> userList = List.of("user1", "user2", "user3", "user4", "user5");

    @Override
    public StreamObserver<SoldOutTicketingRequest> soldOutTicketing(final StreamObserver<SoldOutTicketingResponse> responseObserver) {

        return new StreamObserver<>() {

            @Override
            public void onNext(final SoldOutTicketingRequest request) {
                logger.info("<<< TICKET SERVER >>> sold out ticketing request : {}", request);

                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 모든 user 에게 티켓 매진 소식 알림
                userList.forEach(user ->
                        responseObserver.onNext(
                                SoldOutTicketingResponse.newBuilder()
                                        .setMessage(soldOutMessage(user, request.getTicketId()))
                                        .build()
                        ));
            }

            @Override
            public void onError(final Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                logger.info("<<< TICKET SERVER >>> sold out ticketing completed");
                responseObserver.onCompleted();

            }
        };
    }

    private static String soldOutMessage(final String user, final long ticketId) {
        return String.format("To.%s :ticket %d is sold out now!", user, ticketId);
    }
}
