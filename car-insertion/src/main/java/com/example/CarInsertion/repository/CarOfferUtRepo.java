package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarOfferUtRepo {
    List<InsertionDTO> getCarOfferUtFromUsername(String username);
    int updateCar(Car car);
    int updateOffer(Offer offer);
    int updateUtilities(Utilities ut);
}
