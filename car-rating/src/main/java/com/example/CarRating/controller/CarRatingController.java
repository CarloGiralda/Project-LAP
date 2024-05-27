package com.example.CarRating.controller;

import com.example.CarRating.exception.RatingAlreadyExists;
import com.example.CarRating.model.CarRating;
import com.example.CarRating.model.CarRatingDTO;
import com.example.CarRating.service.CarRatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/carrating")
public class CarRatingController {
    private CarRatingService carRatingService;

    public CarRatingController(CarRatingService carRatingService) {
        super();
        this.carRatingService = carRatingService;
    }

    // id of the car
    @PostMapping(path = "/create")
    public ResponseEntity<?> saveRating(@RequestBody CarRatingDTO dto, @RequestHeader("Logged-In-User") String username) {
        try {
            CarRating rating = carRatingService.convertDTO(dto);
            CarRating res = carRatingService.insertRating(rating);
            if (res != null) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (RatingAlreadyExists e){
            return new ResponseEntity<>("Rating already exists",HttpStatus.CONFLICT);
        }


    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ArrayList<CarRating>> retrieveCarRating(@PathVariable Long id, @RequestHeader("Logged-In-User") String username) {
        ArrayList<CarRating> ratings = carRatingService.retrieveRatings(id);
        if (ratings.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
