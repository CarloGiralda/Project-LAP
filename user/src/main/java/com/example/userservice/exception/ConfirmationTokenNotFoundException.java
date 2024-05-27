package com.example.userservice.exception;

public class ConfirmationTokenNotFoundException extends RuntimeException {
    public ConfirmationTokenNotFoundException(String message) {
        super(message);
    }
}
