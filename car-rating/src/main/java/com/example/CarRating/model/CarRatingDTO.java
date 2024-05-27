package com.example.CarRating.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CarRatingDTO {
    private Long crid;
    private Long stars;
    private LocalDate date;
    private String description;
    // this is the token of the user
    private String madeByUser;
    private Long ratingOnCar;

    public CarRatingDTO() {
    }

    public CarRatingDTO(Long stars, LocalDate date, String desc, String made_by, Long rating_on) {
        this.stars = stars;
        this.date = date;
        this.description = desc;
        this.madeByUser = made_by;
        this.ratingOnCar = rating_on;
    }
}
