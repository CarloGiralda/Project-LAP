package com.example.carbook.exception;

public class UserAlreadySubscribedException extends RuntimeException{

    public UserAlreadySubscribedException(String message) {super(message);
    }
}
