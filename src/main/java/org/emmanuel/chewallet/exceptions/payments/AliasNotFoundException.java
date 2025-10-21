package org.emmanuel.chewallet.exceptions.payments;

public class AliasNotFoundException extends RuntimeException {
    public AliasNotFoundException(String message) {
        super(message);
    }
}
