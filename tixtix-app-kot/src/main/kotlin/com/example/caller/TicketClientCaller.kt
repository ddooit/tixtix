package com.example.caller

import com.example.ticket.TicketingGrpc
import com.example.ticket.TicketingRequest
import io.grpc.ManagedChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TicketClientCaller(
    val channel: ManagedChannel,
    val blockingStub: TicketingGrpc.TicketingBlockingStub = TicketingGrpc.newBlockingStub(channel),
    val logger: Logger = LoggerFactory.getLogger(TicketClientCaller::class.java)
) {

    fun sendUnaryBlockingRequest(): String {

        val ticketingRequest = TicketingRequest.newBuilder()
            .setTicketId(1000)
            .setMemberId(1000)
            .build()
        val response = blockingStub.ticketing(
            ticketingRequest
        )

        logger.info("Unary response: $response")

        return response.message

    }
}