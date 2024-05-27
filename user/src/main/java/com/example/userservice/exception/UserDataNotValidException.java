package com.example.userservice.exception;

public class UserDataNotValidException extends RuntimeException{
    public UserDataNotValidException(String message) {
        super(message);
    }
}
