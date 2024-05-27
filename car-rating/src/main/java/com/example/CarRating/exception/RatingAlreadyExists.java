package com.example.CarRating.exception;

public class RatingAlreadyExists extends RuntimeException{

    public RatingAlreadyExists(String message) {
        super(message);
    }

}
