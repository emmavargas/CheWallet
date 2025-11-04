package org.emmanuel.chewallet.dtos.transactionDto;

public record TransactionDto(
        String transactionId,
        String DestinationName,
        String DestinationLastname,
        String OriginName,
        String OriginLastname,
        String date,
        String type,
        Float amount
) {
}
