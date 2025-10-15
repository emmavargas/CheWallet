package org.emmanuel.chewallet.dtos.payments;

public record PaymentRequestDto(
        String cvu,
        String alias,
        Float amount
) {
}
