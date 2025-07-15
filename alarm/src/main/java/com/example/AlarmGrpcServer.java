package com.example;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class AlarmGrpcServer {
    private static final int ALARM_SERVER_PORT = 50053;

    public static void main(String[] args) throws IOException, InterruptedException {

        final var alarmServer = ServerBuilder.forPort(ALARM_SERVER_PORT)
                .addService(new AlarmGrpcService())
                .build();

        alarmServer.start();
        alarmServer.awaitTermination();
    }
}
