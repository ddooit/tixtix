package com.example

import com.example.dto.TicketingRequestDto
import com.example.caller.TicketClientCaller
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service


@Service
class TixTixService() {

    fun ticketing(requestDto: TicketingRequestDto): String {

        val ticketChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build()

        val resultMessage = TicketClientCaller(ticketChannel).sendUnaryBlockingRequest()

        return resultMessage
    }

}