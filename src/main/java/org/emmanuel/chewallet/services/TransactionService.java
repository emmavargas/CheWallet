package org.emmanuel.chewallet.services;

import org.emmanuel.chewallet.Enums.TransactionType;
import org.emmanuel.chewallet.dtos.transactionDto.PageDto;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionDto;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionDtoRequest;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionDtoResponse;
import org.emmanuel.chewallet.entities.*;
import org.emmanuel.chewallet.exceptions.payments.AliasNotFoundException;
import org.emmanuel.chewallet.exceptions.payments.CvuNotFoundException;
import org.emmanuel.chewallet.exceptions.payments.InsufficientBalanceException;
import org.emmanuel.chewallet.exceptions.payments.SameAccountTransferException;
import org.emmanuel.chewallet.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Transactional
    public TransactionDtoResponse saveTransaction(TransactionDtoRequest transactionDtoRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Account account = user.getAccount();
        Account accountDestination;

        if(transactionDtoRequest.accountDestination().matches("\\d{22}")){
            if(!checkDestinationCvuExists(transactionDtoRequest.accountDestination())){
                throw new CvuNotFoundException("El CVU de destino no existe");
            }
            accountDestination = accountRepository.findByCvu(transactionDtoRequest.accountDestination()).orElseThrow();
        }else{
            if (!checkDestinationAliasExists(transactionDtoRequest.accountDestination())){
                throw new AliasNotFoundException("El alias de destino no existe");
            }
            accountDestination = accountRepository.findByAlias(transactionDtoRequest.accountDestination()).orElseThrow();
        }

        if(!checkIfAmountIsPositive(transactionDtoRequest.amount(), account)){
            throw new InsufficientBalanceException("Fondos insuficientes para realizar la transaccion");
        }
        if(account.getAlias().equals(transactionDtoRequest.accountDestination())){
            throw new SameAccountTransferException("No se puede transferir a la misma cuenta");
        }

        //TRANSACCION DE ENVIO
        Transaction transactionSent = new Transaction();
        transactionSent.setAmount(transactionDtoRequest.amount());
        transactionSent.setAccount(account);
        transactionSent.setAccountOrigin(account);
        transactionSent.setAccountDestination(accountDestination);
        transactionSent.setDate( java.time.LocalDateTime.now());
        transactionSent.setDescription(transactionDtoRequest.description());
        transactionSent.setType(TransactionType.TRANSFER_SENT);
        transactionSent.setStatus("COMPLETED");


        //TRANSACCION DE RECEPCION
        Transaction transactionReceived = new Transaction();
        transactionReceived.setAmount(transactionDtoRequest.amount());
        transactionReceived.setAccount(accountDestination);
        transactionReceived.setAccountOrigin(account);
        transactionReceived.setAccountDestination(accountDestination);
        transactionReceived.setDate( java.time.LocalDateTime.now());
        transactionReceived.setDescription(transactionDtoRequest.description());
        transactionReceived.setType(TransactionType.TRANSFER_RECEIVED);
        transactionReceived.setStatus("COMPLETED");


        transactionRepository.save(transactionSent);
        transactionRepository.save(transactionReceived);
        
        account.getTransactions().add(transactionSent);
        account.setBalance(account.getBalance() - transactionDtoRequest.amount());

        accountDestination.setBalance(accountDestination.getBalance() + transactionDtoRequest.amount());
        accountDestination.getTransactions().add(transactionReceived);

        return new TransactionDtoResponse(
                transactionSent.getTransactionId(),
                account.getCvu(),
                accountDestination.getCvu(),
                transactionSent.getAmount(),
                transactionSent.getDate().toString()
        );

    }



    @Transactional(readOnly = true)
    public PageDto<TransactionDto> getHistoryTransactions(Integer page, Integer size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Account account = user.getAccount();
        Profile profile = user.getProfile();

        Sort sort = Sort.by(Sort.Direction.DESC, "date");

        Pageable pageable = PageRequest.of(page,size,sort);

        Page<TransactionHistoryProjection> pageTransactions = transactionRepository.findHistoryByAccountId(account.getId(), pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        List<TransactionDto> transactionsDto = pageTransactions.stream().map(
                transaction -> new TransactionDto(
                        transaction.getDestinationName(),
                        transaction.getDestinationLastname(),
                        transaction.getOriginName(),
                        transaction.getOriginLastname(),
                        transaction.getTransactionDate().format(formatter),
                        transaction.getTransactionType(),
                        transaction.getAmount()
                )
        ).toList();
        return new PageDto<>(
                transactionsDto,
                pageTransactions.getNumber(),
                pageTransactions.getSize(),
                pageTransactions.getTotalElements(),
                pageTransactions.getTotalPages(),
                pageTransactions.isFirst(),
                pageTransactions.isLast()
        );
    }

    @Transactional(readOnly = true)
    public boolean checkIfAmountIsPositive(Float amount, Account account) {
        return  account.getBalance() >= amount;
    }

    @Transactional(readOnly = true)
    public boolean checkDestinationAliasExists(String accountAlias){
        return accountRepository.existsByAlias(accountAlias);
    }

    public boolean checkDestinationCvuExists(String accountCvu){
        return accountRepository.existsByCvu(accountCvu);
    }

    @Transactional
    public void addAmountToAccount(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow();
            Account account = user.getAccount();
            account.setBalance(account.getBalance() + 5000.0F);
            accountRepository.save(account);
        } catch (Exception e) {
        }

    }

}


