package org.emmanuel.chewallet.services;

import org.emmanuel.chewallet.dtos.transactionDto.DepositDto;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionConfirmDto;
import org.emmanuel.chewallet.entities.Deposit;
import org.emmanuel.chewallet.repositories.DepositRepository;
import org.emmanuel.chewallet.repositories.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final DepositRepository depositRepository;
    private final TransferRepository transferRepository;

    public TransactionService(DepositRepository depositRepository, TransferRepository transferRepository) {
        this.depositRepository = depositRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransactionConfirmDto saveDeposit(DepositDto depositDto) {
        var deposit = new Deposit();
        return null;
    }
}


