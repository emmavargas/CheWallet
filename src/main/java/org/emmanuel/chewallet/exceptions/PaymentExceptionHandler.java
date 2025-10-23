package org.emmanuel.chewallet.exceptions;

import org.emmanuel.chewallet.dtos.ApiErrorDto;
import org.emmanuel.chewallet.exceptions.payments.AliasNotFoundException;
import org.emmanuel.chewallet.exceptions.payments.InsufficientBalanceException;
import org.emmanuel.chewallet.exceptions.payments.SameAccountTransferException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class PaymentExceptionHandler {



    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handlePaymentException(InsufficientBalanceException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                402,
                "Payment Required",
                Map.of("error", ex.getMessage())
        );
        return ResponseEntity.status(402).body(apiErrorDto);
    }

    @ExceptionHandler(AliasNotFoundException.class)
    public ResponseEntity<?> handleAliasNotFoundException(AliasNotFoundException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                404,
                "Account Not Found",
                Map.of("error", ex.getMessage())
        );
        return ResponseEntity.status(404).body(apiErrorDto);
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<?> handleSameAccountTransferException(SameAccountTransferException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                400,
                "Same Account Transfer",
                Map.of("error", ex.getMessage())
        );
        return ResponseEntity.status(400).body(apiErrorDto);
    }
}
