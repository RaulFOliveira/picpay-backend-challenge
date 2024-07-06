package com.example.picpay_challenge.infra.exceptions;

public class InsufficentBalanceException extends RuntimeException {
    public InsufficentBalanceException(String message) {
        super(message);
    }
}
