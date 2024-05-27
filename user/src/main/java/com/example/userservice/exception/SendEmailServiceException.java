package com.example.userservice.exception;

public class SendEmailServiceException extends RuntimeException {
    public SendEmailServiceException(String message) {
        super(message);
    }
}
