package org.emmanuel.chewallet.exceptions;

import org.emmanuel.chewallet.dtos.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1 + ", " + msg2 // si hay más de un error en el mismo campo
                ));

        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errores
        );
        return ResponseEntity.badRequest().body(apiErrorDto);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                409,
                "Conflict",
                Map.of("username", ex.getMessage())
        );
        return ResponseEntity.status(409).body(apiErrorDto);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(

                LocalDateTime.now(),
                409,
                "Conflict",
                Map.of("email", ex.getMessage())
        );
        return ResponseEntity.status(409).body(apiErrorDto);
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<?> handlePasswordMismatch(PasswordMismatchException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Password Mismatch",
                Map.of("password", ex.getMessage())
        );
        return ResponseEntity.badRequest().body(apiErrorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                Map.of("message", "Ha ocurrido un error inesperado")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorDto);
    }

}
