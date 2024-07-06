package com.example.picpay_challenge.infra.exceptions;

public class TransferNotAuthorizedException extends RuntimeException {
    public TransferNotAuthorizedException(String message) {
        super(message);
    }
}
