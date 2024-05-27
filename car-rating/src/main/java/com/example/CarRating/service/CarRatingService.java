package com.example.CarRating.service;

import com.example.CarRating.model.CarRating;
import com.example.CarRating.model.CarRatingDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

public interface CarRatingService {
    public CarRating insertRating(CarRating carRating);
    public CarRating convertDTO(CarRatingDTO dto);
    public ArrayList<CarRating> retrieveRatings(Long id);
}
