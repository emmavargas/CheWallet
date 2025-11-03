package org.emmanuel.chewallet.controllers;

import org.emmanuel.chewallet.dtos.transactionDto.TransactionDtoResponse;
import org.emmanuel.chewallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDtoResponse> getTransaction(@PathVariable Long transactionId){
        TransactionDtoResponse transactionDto = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transactionDto);
    }

}
