package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDtoRequest(
        String accountDestination,
        float amount,
        String description
) {
}
