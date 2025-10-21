package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDto(
        String name,
        String lastname,
        String date,
        String type,
        Float amount
) {
}
