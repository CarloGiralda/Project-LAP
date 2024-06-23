package com.example.CarInsertion.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuctionDTO {
    private Car car;
    private Auction auction;
    private Utilities utilities;

    public AuctionDTO() {
    }

    public AuctionDTO(Car car, Auction auction, Utilities utilities) {
        this.car = car;
        this.auction = auction;
        this.utilities = utilities;
    }
}
