package org.emmanuel.chewallet.controllers;

import org.emmanuel.chewallet.dtos.payments.CheckoutResponseDto;
import org.emmanuel.chewallet.dtos.payments.PaymentRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusDto;
import org.emmanuel.chewallet.dtos.payments.webHookDto.MercadoPagoWebhookDto;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionDtoRequest;
import org.emmanuel.chewallet.dtos.transactionDto.TransactionDtoResponse;
import org.emmanuel.chewallet.services.PaymentService;
import org.emmanuel.chewallet.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    public PaymentController(PaymentService paymentService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.transactionService = transactionService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> createCheckout(@RequestBody PaymentRequestDto paymentRequestDto) {
        String preferenceId = paymentService.createCheckoutPreference(paymentRequestDto);
        return ResponseEntity.ok(new CheckoutResponseDto(preferenceId));
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentStatusDto> confirmPayment(@RequestBody PaymentStatusRequestDto confirmDto) {
        PaymentStatusDto statusDto = paymentService.processPaymentStatus(confirmDto);
        switch (statusDto.status()) {
            case "approved" -> {
                return ResponseEntity.ok(statusDto);
            }
            case "pending", "in_process" -> {
                return ResponseEntity.status(202).body(statusDto); // 202 Accepted para pendiente
            }
            case "rejected" -> {
                return ResponseEntity.badRequest().body(statusDto);
            }
            default -> {
                return ResponseEntity.status(500).body(statusDto);
            }
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody MercadoPagoWebhookDto payload) {
        System.out.println("Webhook recibido: " + payload);
        String paymentId = payload.data() != null && payload.data().id() != null
                ? payload.data().id()
                : payload.id();
        if (paymentId != null) {
            System.out.println("Procesando paymentId: " + paymentId);
            PaymentStatusRequestDto paymentStatusRequestDto = new PaymentStatusRequestDto(paymentId);
            PaymentStatusDto result = paymentService.processPaymentStatus(paymentStatusRequestDto);
            System.out.println("Resultado del procesamiento: " + result.status() + " - " + result.message());
        } else {
            System.out.println("No se encontró paymentId en el payload");
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/transfer")
    public ResponseEntity<?> createTransfer(@RequestBody TransactionDtoRequest transactionDtoRequest) {
        TransactionDtoResponse transactionDtoResponse = transactionService.saveTransaction(transactionDtoRequest);
        return ResponseEntity.ok(transactionDtoResponse);
    }



    @GetMapping("/transactions")
    public ResponseEntity<?> getTransaction(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(transactionService.getHistoryTransactions(page, size));
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionDtoResponse> getTransaction(@PathVariable("transactionId") Long transactionId){
        TransactionDtoResponse transactionDto = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transactionDto);
    }

    @PostMapping("/amount")
    public ResponseEntity<?> addAmount(@RequestBody TransactionDtoRequest transactionDtoRequest) {
        transactionService.addAmountToAccount();
        Map<String,String> response = Map.of("message", "Monto agregado exitosamente");
        return ResponseEntity.ok(response);
    }
}
