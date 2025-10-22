package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDtoResponse(
        String operationNumber,
        String originCvu,
        String destinationCvu,
        Float amount,
        String date
) {
}
