package com.example.dto;

public record BulkTicketingResponse(
        int ticketCount,
        long totalPrice
) {
    public static BulkTicketingResponse fromGrpcResponse(final com.example.ticket.BulkTicketingResponse grpcResponse) {
        return new BulkTicketingResponse(
                grpcResponse.getTicketCount(),
                grpcResponse.getTotalPrice()
        );
    }
}
