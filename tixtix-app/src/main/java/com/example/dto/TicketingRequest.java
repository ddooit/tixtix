package com.example.dto;

public record TicketingRequest(
        long ticketId,
        long memberId
) {
    public com.example.ticket.TicketingRequest toGrpcRequest() {
        return com.example.ticket.TicketingRequest.newBuilder()
                .setTicketId(ticketId)
                .setMemberId(memberId)
                .build();
    }
}
