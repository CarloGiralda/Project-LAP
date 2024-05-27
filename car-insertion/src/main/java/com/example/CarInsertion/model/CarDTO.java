package com.example.CarInsertion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private String plateNum;
    private String year;
    private Car.PollutionLevel pollutionLevel;
    private Car.Fuel fuel;
    private String brand;
    private String passengers;
    private String model;
    private Car.Classification classification;
    private String carDoorNumber;
    private Car.Engine engine;
    private Car.Transmission transmission;
    private MultipartFile image;
}
