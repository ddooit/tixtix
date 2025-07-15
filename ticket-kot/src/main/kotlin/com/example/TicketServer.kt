package com.example

import io.grpc.ServerBuilder

fun main() {
    val server = ServerBuilder
        .forPort(50051)
        .addService(TicketService())
        .build()

    server.start()
    server.awaitTermination()
}