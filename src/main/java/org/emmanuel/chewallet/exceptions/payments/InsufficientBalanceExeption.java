package org.emmanuel.chewallet.exceptions.payments;

public class InsufficientBalanceExeption extends RuntimeException {
    public InsufficientBalanceExeption(String message) {
        super(message);
    }
}
