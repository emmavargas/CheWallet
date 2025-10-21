package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDtoRequest(
        String alias,
        float amount,
        String description
) {
}
