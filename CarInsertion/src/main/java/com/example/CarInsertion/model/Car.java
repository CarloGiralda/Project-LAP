package com.example.CarInsertion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "car")
public class Car {
    public enum Engine {
        Electric,
        Gas,
        Diesel,
        Hybrid
    }

    public enum Size {
        Big,
        Medium,
        Small
    }

    public enum Transmission {
        Manual,
        Automatic
    }

    @Id
    @SequenceGenerator(name = "car_gen", sequenceName = "car_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_gen")
    private Long cid;
    private String plateNum;
    private Long year;
    private String pollutionLevel;
    private String fuel;
    private String brand;
    private Long passengers;
    private String model;
    private String position;
    private String insurance;
    private Size size;
    private Long carDoorNumber;
    private Engine engine;
    private Transmission transmission;
    private Long offer_id;
    private Long optionals_utid;

    public Car() {
    }

    public Car(Long cid) {
        this.cid = cid;
    }

    public Car(String plateNum, Long year, String pollutionLevel, String fuel, String brand, Long passengers, String model,
               String position, String insurance, Size size, Long carDoorNumber, Engine engine, Transmission transmission) {
        this.plateNum = plateNum;
        this.year = year;
        this.pollutionLevel = pollutionLevel;
        this.fuel = fuel;
        this.brand = brand;
        this.passengers = passengers;
        this.model = model;
        this.position = position;
        this.insurance = insurance;
        this.size = size;
        this.carDoorNumber = carDoorNumber;
        this.engine = engine;
        this.transmission = transmission;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public void setOffer_id(Long offer_id) {
        this.offer_id = offer_id;
    }

    public void setOptionals_utid(Long optionals_utid) {
        this.optionals_utid = optionals_utid;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public Long getYear() {
        return year;
    }

    public String getPollutionLevel() {
        return pollutionLevel;
    }

    public String getFuel() {
        return fuel;
    }

    public String getBrand() {
        return brand;
    }

    public Long getPassengers() {
        return passengers;
    }

    public String getModel() {
        return model;
    }

    public String getPosition() {
        return position;
    }

    public String getInsurance() {
        return insurance;
    }

    public Size getSize() {
        return size;
    }

    public Long getCarDoorNumber() {
        return carDoorNumber;
    }

    public Engine getEngine() {
        return engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }
}
