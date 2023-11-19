package com.example.CarSearch.model;

import java.util.List;

public class ResponseDTO {
    private List<Car> cars;
    private List<Offer> offers;
    private List<Utilities> utilities;

    public ResponseDTO(List<Car> cars, List<Offer> offers, List<Utilities> utilities) {
        this.cars = cars;
        this.offers = offers;
        this.utilities = utilities;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public List<Utilities> getUtilities() {
        return utilities;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void setUtilities(List<Utilities> utilities) {
        this.utilities = utilities;
    }

    public void addCar(Car car) { this.cars.add(car); }

    public void addOffer(Offer offer) { this.offers.add(offer); }

    public void addUtilities(Utilities utilities) { this.utilities.add(utilities); }
}
