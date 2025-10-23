package org.emmanuel.chewallet.exceptions.payments;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException(String message) {
        super(message);
    }
}
