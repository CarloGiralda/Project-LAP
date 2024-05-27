package com.example.CarSearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarPreviewDTO {

    private String brand;
    private String model;
    private Car.Engine engine;
    private Long year;
}
