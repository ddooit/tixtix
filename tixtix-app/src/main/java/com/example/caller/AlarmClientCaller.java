package com.example.caller;

import com.example.alarm.AlarmServiceGrpc;
import com.example.alarm.SendAlarmRequest;
import com.example.payment.Empty;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmClientCaller {

    private static final Logger logger = LoggerFactory.getLogger(AlarmClientCaller.class);

    private final AlarmServiceGrpc.AlarmServiceFutureStub futureStub;

    AlarmClientCaller(final ManagedChannel channel) {
        this.futureStub = AlarmServiceGrpc.newFutureStub(channel);
    }

    public ListenableFuture<Empty> callAlarmSend(final SendAlarmRequest request) {
        logger.info("<<< ALARM CLIENT >>> send alarm request : {}", request);

        return futureStub.sendAlarm(request);
    }

}
