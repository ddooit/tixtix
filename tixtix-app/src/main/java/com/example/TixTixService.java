package com.example;

import com.example.dto.PaymentRequest;
import com.example.dto.TicketingRequest;
import com.example.dto.TicketingResponse;
import com.example.payment.Empty;
import com.example.ticket.TicketingStatus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.caller.ServerCallerFactory.paymentClientCaller;
import static com.example.caller.ServerCallerFactory.ticketClientCaller;

@Service
class TixTixService {
    private final static Logger logger = LoggerFactory.getLogger(TixTixService.class);
    private final static ExecutorService callbackExecutor = Executors.newCachedThreadPool();

    public TicketingResponse ticketing(final TicketingRequest request) {
        // 1. ticket server 호출
        final var ticketingGrpcResponse = ticketClientCaller()
                .callTicketingRequest(request);

        // 6. ticketing 실패했다면, failed 응답 포워딩
        if (ticketingGrpcResponse.getStatus() == TicketingStatus.FAILED) {
            return new TicketingResponse(TicketingStatus.FAILED, ticketingGrpcResponse.getMessage());
        }

        logger.info("<<< TIX TIX >>> ticketing response : {}", ticketingGrpcResponse);

        // 4. payment server 호출 : 결제 대기 상태로 전환 요청
        final var paymentGetReady = paymentClientCaller()
                .callPaymentGetReady(
                        new PaymentRequest(ticketingGrpcResponse.getMemberId(), ticketingGrpcResponse.getTicketPrice()));

        Futures.addCallback(paymentGetReady, new FutureCallback<>() {
            @Override
            public void onSuccess(final Empty result) {
                logger.info("결제 대기 처리 성공 : {}", result);
            }

            @Override
            public void onFailure(@NotNull final Throwable t) {
                logger.info("결제 대기 처리 실패 : {}", t.getMessage());

            }
        }, callbackExecutor);

//        logger.info("<<< TIX TIX >>> END : {}", ticketingGrpcResponse);

        return new TicketingResponse(TicketingStatus.PAYMENT_PROCESSING, "");

    }

}
