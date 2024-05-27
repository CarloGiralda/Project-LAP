package com.example.CarRating.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "car_rating",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"made_by_user", "rating_on_car"})})
public class CarRating {
    @Id
    @SequenceGenerator(name = "carrat_gen", sequenceName = "carrat_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carrat_gen")
    private Long crid;
    private Long stars;
    private LocalDate date;
    private String description;
    private Long madeByUser;
    private Long ratingOnCar;

    public CarRating() {
    }

    public CarRating(Long stars, LocalDate date, String desc, Long made_by, Long rating_on) {
        this.stars = stars;
        this.date = date;
        this.description = desc;
        this.madeByUser = made_by;
        this.ratingOnCar = rating_on;
    }
}
