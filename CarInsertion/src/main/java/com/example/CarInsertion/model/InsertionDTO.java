package com.example.CarInsertion.model;

import jakarta.persistence.Entity;

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

    public Car getCar() {
        return car;
    }

    public Offer getOffer() {
        return offer;
    }

    public Utilities getUtilities() {
        return utilities;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setUtilities(Utilities utilities) {
        this.utilities = utilities;
    }
}
