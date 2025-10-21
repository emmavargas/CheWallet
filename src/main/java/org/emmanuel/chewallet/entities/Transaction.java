package org.emmanuel.chewallet.entities;

import jakarta.persistence.*;
import org.emmanuel.chewallet.Enums.TransactionType;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "account_origin_id", referencedColumnName = "id")
    private Account accountOrigin;

    @ManyToOne()
    @JoinColumn(name = "account_destionation__id", referencedColumnName = "id")
    private Account accountDestination;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;

    @Column(nullable = false)
    private Float amount;

    private String Status;

    @Column(nullable = true)
    private String externalReference;

    public String transactionId;

    private LocalDateTime date = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null) {
            this.transactionId = java.util.UUID.randomUUID().toString();
        }
    }


    public Transaction() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccountOrigin() {
        return accountOrigin;
    }

    public void setAccountOrigin(Account accountOrigin) {
        this.accountOrigin = accountOrigin;
    }

    public Account getAccountDestination() {
        return accountDestination;
    }

    public void setAccountDestination(Account accountDestination) {
        this.accountDestination = accountDestination;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
