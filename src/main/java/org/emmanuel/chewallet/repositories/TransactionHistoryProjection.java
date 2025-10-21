package org.emmanuel.chewallet.repositories;

import java.time.LocalDate;

public interface TransactionHistoryProjection {
    Long getTransactionId();
    Long getAccountId();
    String getDestinationName();
    String getDestinationLastname();
    Float getAmount();
    String getTransactionType();
    java.time.LocalDateTime getTransactionDate();
}
