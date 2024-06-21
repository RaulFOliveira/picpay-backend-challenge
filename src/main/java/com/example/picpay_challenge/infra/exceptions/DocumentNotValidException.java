package com.example.picpay_challenge.infra.exceptions;

public class DocumentNotValidException extends RuntimeException {
    public DocumentNotValidException(String message) {
        super(message);
    }
}
