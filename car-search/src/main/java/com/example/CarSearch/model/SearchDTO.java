package com.example.CarSearch.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchDTO {
    private Car car;
    private Offer offer;
    private Utilities utilities;

    public SearchDTO() {
    }

    public SearchDTO(Car car, Offer offer, Utilities utilities) {
        this.car = car;
        this.offer = offer;
        this.utilities = utilities;
    }
}
