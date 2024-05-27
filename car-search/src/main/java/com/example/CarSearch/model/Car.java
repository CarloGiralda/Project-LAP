package com.example.CarSearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "car")
public class Car {
    public enum Engine {
        Electric,
        Gas,
        Diesel,
        Hybrid
    }

    public enum Classification {
        CityCar,
        SubcompactExecutive,
        CompactExecutive,
        FullSize,
        FullSizeLuxury
    }

    public enum Transmission {
        Manual,
        Automatic
    }

    public enum Fuel {
        Gas,
        Methane,
        Diesel,
        GPL,
        Electric
    }

    public enum PollutionLevel {
        EURO2,
        EURO3,
        EURO4,
        EURO5,
        EURO6,
    }

    @Id
    @SequenceGenerator(name = "car_seq", sequenceName = "car_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq")
    private Long cid;
    private String plateNum;
    private Long year;
    private PollutionLevel pollutionLevel;
    private Fuel fuel;
    private String brand;
    private Long passengers;
    private String model;
    private Classification classification;
    private Long carDoorNumber;
    private Engine engine;
    private Transmission transmission;
    private byte[] image;
    @OneToOne
    @JoinColumn(name = "offer_oid")
    private Offer offer_oid;
    @OneToOne
    @JoinColumn(name = "utilities_utid")
    private Utilities utilities_utid;

    public Car() {
    }

    public Car(Long cid) {
        this.cid = cid;
    }

    public Car(String plateNum, Long year, PollutionLevel pollutionLevel, Fuel fuel, String brand, Long passengers, String model,
               Classification classification, Long carDoorNumber, Engine engine, Transmission transmission, byte[] image) {
        this.plateNum = plateNum;
        this.year = year;
        this.pollutionLevel = pollutionLevel;
        this.fuel = fuel;
        this.brand = brand;
        this.passengers = passengers;
        this.model = model;
        this.classification = classification;
        this.carDoorNumber = carDoorNumber;
        this.engine = engine;
        this.transmission = transmission;
        this.image = image;
    }
}
