package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDtoResponse(
        String operationNumber,
        String destinationCvu,
        String destinationAlias,
        Float amount,
        String date
) {
}
