package org.emmanuel.chewallet.dtos.transactionDto;

public record DepositDto(
        String cvu,
        String alias,
        float amount
) {
}
