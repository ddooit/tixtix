package com.example.dto;

public record SoldOutTicketingRequest(
        long ticketId
) {
    public com.example.ticket.SoldOutTicketingRequest toGrpcRequest() {
        return com.example.ticket.SoldOutTicketingRequest.newBuilder()
                .setTicketId(ticketId)
                .build();
    }
}
