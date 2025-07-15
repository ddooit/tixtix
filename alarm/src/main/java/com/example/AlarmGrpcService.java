package com.example;

import com.example.alarm.AlarmServiceGrpc;
import com.example.alarm.SendAlarmRequest;
import com.example.payment.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmGrpcService extends AlarmServiceGrpc.AlarmServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AlarmGrpcService.class);

    @Override
    public void sendAlarm(final SendAlarmRequest request, final StreamObserver<Empty> responseObserver) {
        logger.info("<<< ALARM SERVER >>> send alarm request : {}", request);
        // todo. something..

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();

    }
}
