package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDto(
        String DestinationName,
        String DestinationLastname,
        String OriginName,
        String OriginLastname,
        String date,
        String type,
        Float amount
) {
}
