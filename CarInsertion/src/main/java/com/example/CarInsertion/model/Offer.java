package com.example.CarInsertion.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @SequenceGenerator(name = "offer_gen", sequenceName = "offer_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_gen")
    private Long oid;
    private boolean available;
    private LocalDate fromDate;
    private LocalDate toDate;
    // TODO Il tipo float dà problemi con JSON
    private String pricePerDay;
    // TODO Devono essere implementati prendendo dati dagli altri servizi
    /*
    private Long renter_id;
    private Long zoneLocation;
    */

    public Offer() {
    }

    public Offer(LocalDate fromDate, LocalDate toDate, String pricePerDay) {
        this.available = true;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pricePerDay = pricePerDay;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }
}
