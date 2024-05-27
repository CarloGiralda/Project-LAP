package com.example.userservice.exception;

public class ConfirmationTokenExpiredException extends RuntimeException {
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
