package com.example

import com.example.ticket.TicketingGrpc
import com.example.ticket.TicketingRequest
import com.example.ticket.TicketingResponse
import com.example.ticket.TicketingStatus
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory

class TicketService : TicketingGrpc.TicketingImplBase() {

    override fun ticketing(
        request: TicketingRequest?,
        responseObserver: StreamObserver<TicketingResponse?>?
    ) {
        LoggerFactory.getLogger(TicketService::class.java)
            .info("<<< TICKET SERVER >>> ticketing request : $request")

        TicketingResponse.newBuilder()
            .setStatus(TicketingStatus.PAYMENT_PROCESSING)
            .setMessage("Ticketing successful!")
            .build()
            .let {
                responseObserver?.onNext(it)
                responseObserver?.onCompleted()
            }
    }
}