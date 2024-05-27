package com.example.CarRating.repository;

import com.example.CarRating.model.CarRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CarRatingRepo extends JpaRepository<CarRating, Long> {
    ArrayList<CarRating> findCarRatingByRatingOnCar(Long id);
}
