package com.example.CarInsertion.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InsertionDTO {
    private Car car;
    private Offer offer;
    private Utilities utilities;

    public InsertionDTO() {
    }

    public InsertionDTO(Car car, Offer offer, Utilities utilities) {
        this.car = car;
        this.offer = offer;
        this.utilities = utilities;
    }

}
