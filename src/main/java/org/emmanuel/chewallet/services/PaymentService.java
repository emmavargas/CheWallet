package org.emmanuel.chewallet.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.emmanuel.chewallet.dtos.payments.PaymentRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusRequestDto;
import org.emmanuel.chewallet.dtos.payments.PaymentStatusDto;
import org.emmanuel.chewallet.entities.Transaction;
import org.emmanuel.chewallet.repositories.AccountRepository;
import org.emmanuel.chewallet.repositories.TransactionRepository;
import org.emmanuel.chewallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Value("${TOKEN_MP}")
    private String TOKEN_MP;

    public PaymentService(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public String createCheckoutPreference(PaymentRequestDto paymentRequestDto) {
        try {
            MercadoPagoConfig.setAccessToken(TOKEN_MP);
            PreferenceClient client = new PreferenceClient();

            String externalReference = "deposit_" + java.util.UUID.randomUUID();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Recarga en CheWallet")
                    .description("Recarga para usuario CVU: " + paymentRequestDto.cvu())
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(paymentRequestDto.amount()))
                    .build();
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://8f2c67f18ee6.ngrok-free.app/checkout-test.html")
                .failure("http://localhost:8080/checkout-test.html")
                .pending("http://localhost:8080/checkout-test.html")
                .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(java.util.Collections.singletonList(item))
                    .externalReference(externalReference)
                    .backUrls(backUrls)
                    .build();

            Preference preference = client.create(request);

            String identificador = preference.getExternalReference();
            createDeposit(paymentRequestDto, identificador);
            return preference.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la preferencia de pago en Mercado Pago", e);
        }
    }

    public PaymentStatusDto processPaymentStatus(PaymentStatusRequestDto paymentStatusRequestDto) {
        try {
            MercadoPagoConfig.setAccessToken(TOKEN_MP);
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.parseLong(paymentStatusRequestDto.paymentId()));
            System.out.println(payment);
            String status = payment.getStatus();
            String message;
            switch (status) {
                case "approved" -> {
                    var deposit = transactionRepository.findByExternalReference(payment.getExternalReference()).orElseThrow();
                    var account = deposit.getAccount();
                    account.setBalance(account.getBalance() + deposit.getAmount());
                    deposit.setStatus("APPROVED");
                    deposit.setTransactionId(paymentStatusRequestDto.paymentId());
                    accountRepository.save(account);
                    transactionRepository.save(deposit);
                    message = "Pago confirmado y saldo actualizado.";
                }
                case "pending", "in_process" ->{
                    var deposit = transactionRepository.findByExternalReference(payment.getExternalReference()).orElseThrow();
                    deposit.setTransactionId(paymentStatusRequestDto.paymentId());
                    deposit.setStatus("PENDING");
                    transactionRepository.save(deposit);
                    message = "Pago pendiente de acreditación. Te avisaremos cuando se confirme.";
                }
                case "rejected" ->{
                    var deposit = transactionRepository.findByExternalReference(payment.getExternalReference()).orElseThrow();
                    deposit.setTransactionId(paymentStatusRequestDto.paymentId());
                    deposit.setStatus("REJECTED");
                    transactionRepository.save(deposit);
                    message = "Pago rechazado. Intenta nuevamente.";
                }
                case null, default -> message = "Estado de pago desconocido: " + status;
            }
            return new PaymentStatusDto(status, message, paymentStatusRequestDto.paymentId());
        } catch (Exception e) {
            return new PaymentStatusDto("error", "Error al confirmar el pago en Mercado Pago", paymentStatusRequestDto.paymentId());
        }
    }

    public void createDeposit(PaymentRequestDto paymentRequestDto, String identificador) {
        var deposit = new Transaction();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        if(!user.getAccount().getCvu().equals(paymentRequestDto.cvu())) {
            throw new RuntimeException("El CVU no corresponde al usuario autenticado");
        }
        deposit.setAmount(paymentRequestDto.amount());
        deposit.setStatus("INITIATED");
        deposit.setDate(java.time.LocalDateTime.now());
        deposit.setExternalReference(identificador);
        var account = user.getAccount();
        deposit.setAccountDestination(account);
        deposit.setAccount(account);
        account.getTransactions().add(deposit);
        transactionRepository.save(deposit);
    }
}
