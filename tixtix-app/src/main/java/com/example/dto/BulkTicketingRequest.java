package com.example.dto;

public record BulkTicketingRequest(
        long ticketId
) {
    public com.example.ticket.BulkTicketingRequest toGrpcRequest() {
        return com.example.ticket.BulkTicketingRequest.newBuilder()
                .setTicketId(ticketId)
                .build();
    }
}
