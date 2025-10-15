package org.emmanuel.chewallet.dtos.payments;

public record PaymentStatusDto(
    String status,
    String message,
    String paymentId
) {}

