package org.emmanuel.chewallet.exceptions.payments;

import org.emmanuel.chewallet.dtos.ApiErrorDto;
import org.hibernate.sql.Alias;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice(basePackages = "org.emmanuel.chewallet.controllers.payment")
public class PaymentExceptionHandler {



    @ExceptionHandler(InsufficientBalanceExeption.class)
    public ResponseEntity<?> handlePaymentException(InsufficientBalanceExeption ex) {
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
}
