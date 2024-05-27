package com.example.CarSearch.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
public class ReturnDTO {
    private Long cid;
    private String brand;
    private String model;
    private String pricePerHour;
    private byte[] image;

    public ReturnDTO(Long cid, String brand, String model, String pricePerHour, byte[] image) {
        this.cid = cid;
        this.brand = brand;
        this.model = model;
        this.pricePerHour = pricePerHour;
        this.image = image;
    }
}
