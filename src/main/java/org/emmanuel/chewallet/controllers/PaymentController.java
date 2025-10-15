package org.emmanuel.chewallet.controllers;

import org.emmanuel.chewallet.dtos.payments.CheckoutResponseDto;
import org.emmanuel.chewallet.dtos.payments.PaymentRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusDto;
import org.emmanuel.chewallet.dtos.payments.webHookDto.MercadoPagoWebhookDto;
import org.emmanuel.chewallet.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
}
