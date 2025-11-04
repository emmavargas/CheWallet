package org.emmanuel.chewallet.repositories;

public interface TransactionHistoryProjection {
    String getTransactionId();
    String getDestinationName();
    String getDestinationLastname();
    String getOriginName();
    String getOriginLastname();
    Float getAmount();
    String getTransactionType();
    java.time.LocalDateTime getTransactionDate();
}
