package org.emmanuel.chewallet.dtos.UserDataDto;

import org.emmanuel.chewallet.dtos.transactionDto.TransactionDto;

import java.util.List;

public record ResumeUserDto(
        String name,
        String lastname,
        String cvu,
        String alias,
        Float balance,
        List<TransactionDto> recentTransactions
) {
}
