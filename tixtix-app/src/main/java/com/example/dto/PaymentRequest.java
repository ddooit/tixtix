package com.example.dto;

public record PaymentRequest(
        long memberId,
        int price
) {
}
