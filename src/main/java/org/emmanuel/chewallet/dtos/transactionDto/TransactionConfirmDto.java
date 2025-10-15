package org.emmanuel.chewallet.dtos.transactionDto;

import java.time.LocalDateTime;

public record TransactionConfirmDto(
        String operationNumber,
        String destinationCvu,
        String destinationAlias,
        float amount,
        LocalDateTime date
) {
}
