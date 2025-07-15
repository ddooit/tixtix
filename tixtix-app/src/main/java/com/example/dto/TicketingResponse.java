package com.example.dto;

import com.example.ticket.TicketingStatus;

public record TicketingResponse(
        TicketingStatus status,
        String message
) {
//    public static TicketingResponse fromGrpcResponse(final com.example.ticket.TicketingResponse grpcResponse) {
//        return new TicketingResponse(
//                TicketingStatus.valueOf(grpcResponse.getStatus().name()),
//                grpcResponse.getMessage()
//        );
//    }
}
