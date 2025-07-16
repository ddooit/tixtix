package com.example;

import com.example.alarm.AlarmType;
import com.example.alarm.SendAlarmRequest;
import com.example.dto.BulkTicketingRequest;
import com.example.dto.BulkTicketingResponse;
import com.example.dto.TicketingRequest;
import com.example.dto.TicketingResponse;
import com.example.payment.Empty;
import com.example.payment.PaymentReadyRequest;
import com.example.dto.SoldOutTicketingRequest;
import com.example.ticket.TicketingStatus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.caller.ServerCallerFactory.*;

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
        // 논블럭, 콜백 호출
        paymentProcessing(ticketingGrpcResponse);

        // 5. alarm server 호출 : 사용자에게 알림 전송 요청
        // 논블럭, 콜백 호출
        alarmProcessing(ticketingGrpcResponse.getMemberId());

        logger.info("<<< TIX TIX >>> END : {}", ticketingGrpcResponse);

        return new TicketingResponse(TicketingStatus.PAYMENT_PROCESSING, "");
    }

    private void paymentProcessing(final com.example.ticket.TicketingResponse response) {
        final var paymentGetReadyFuture = paymentClientCaller()
                .callPaymentGetReady(
                        PaymentReadyRequest.newBuilder()
                                .setMemberId(response.getMemberId())
                                .setPrice(response.getTicketPrice())
                                .build());

        Futures.addCallback(paymentGetReadyFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(final Empty result) {
                logger.info("결제 대기 처리 성공 : {}", result);
            }

            @Override
            public void onFailure(final Throwable t) {
                logger.info("결제 대기 처리 실패 : {}", t.getMessage());

            }
        }, callbackExecutor);
    }

    private void alarmProcessing(final long memberId) {
        final var sendAlarmFuture = alarmClientCaller()
                .callAlarmSend(
                        SendAlarmRequest.newBuilder()
                                .setAlarmType(AlarmType.TICKETING)
                                .setMemberId(memberId)
                                .build());

        Futures.addCallback(sendAlarmFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(final Empty result) {
                logger.info("알림 전송 성공 : {}", result);
            }

            @Override
            public void onFailure(final Throwable t) {
                logger.info("알림 전송 실패 : {}", t.getMessage());

            }
        }, callbackExecutor);
    }

    public List<Long> monitoring(final long performanceId) {
        final var result = new ArrayList<Long>();

        ticketClientCaller()
                .callMonitoring(performanceId)
                .forEachRemaining(monitoringResponse -> {

                    alarmProcessing(1_000); // todo. fix it for monitoring

                    result.add(monitoringResponse.getRemainingTicketCount());
                });

        return result;
    }

    public BulkTicketingResponse bulkTicketing(final List<BulkTicketingRequest> ticketingRequests) {
        final var requests = ticketingRequests.stream()
                .map(BulkTicketingRequest::toGrpcRequest).toList();

        return BulkTicketingResponse.fromGrpcResponse(ticketClientCaller()
                .callBulkTicketingRequest(requests));
    }

    public void soldOutTicketing(final List<SoldOutTicketingRequest> soldOutTicketingRequests) {
        final var requests = soldOutTicketingRequests.stream()
                .map(SoldOutTicketingRequest::toGrpcRequest)
                .toList();

        ticketClientCaller()
                .callSoldOutTicketing(requests);
    }
}
