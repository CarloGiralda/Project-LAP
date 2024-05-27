package com.example.CarSearch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @SequenceGenerator(name = "offer_seq", sequenceName = "offer_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_seq")
    private Long oid;
    private Boolean available;
    private Long fromDate;
    private Long toDate;
    // TODO Il tipo float dà problemi con JSON
    private String pricePerHour;
    private String renterUsername;
    private String zoneLocation;

    public Offer() {
        this.available = true;
    }

    public Offer(Long fromDate, Long toDate, String pricePerHour, String renterUsername, String zoneLocation) {
        this.available = true;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pricePerHour = pricePerHour;
        this.renterUsername = renterUsername;
        this.zoneLocation = zoneLocation;
    }
}
