package com.example.picpay_challenge.infra.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentAlreadyExistsException extends RuntimeException {
    public DocumentAlreadyExistsException(String message) {
        super(message);
    }
}
