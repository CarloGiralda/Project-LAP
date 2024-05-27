package com.example.CarInsertion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@DynamicUpdate
@Setter
@Getter
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

    public boolean hasEmptyFields() {
        return getFromDate() == null || getToDate() == null || getPricePerHour() == null || getRenterUsername() == null || getZoneLocation() == null;
    }
}
