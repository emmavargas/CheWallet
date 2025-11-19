package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDtoResponse(
        String operationNumber,
        String originCvu,
        String originName,
        String originLastname,
        String destinationCvu,
        String destinationName,
        String destinationLastname,
        Float amount,
        String date,
        String description
) {
}
