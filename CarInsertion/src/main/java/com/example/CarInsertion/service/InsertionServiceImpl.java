package com.example.CarInsertion.service;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.repository.CarRepo;
import com.example.CarInsertion.repository.OfferRepo;
import com.example.CarInsertion.repository.UtilitiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertionServiceImpl implements InsertionService{
    private CarRepo carRepo;
    private OfferRepo offerRepo;
    private UtilitiesRepo utilitiesRepo;

    @Autowired
    public InsertionServiceImpl(CarRepo carRepo, OfferRepo offerRepo, UtilitiesRepo utilitiesRepo) {
        this.carRepo = carRepo;
        this.offerRepo = offerRepo;
        this.utilitiesRepo = utilitiesRepo;
    }

    @Override
    public InsertionDTO insert(InsertionDTO dto) {
        Offer offer = dto.getOffer();
        Utilities ut = dto.getUtilities();
        Car car = dto.getCar();
        Offer resultOffer = offerRepo.save(offer);
        Utilities resultUt = utilitiesRepo.save(ut);
        car.setOffer_id(offer.getOid());
        car.setOptionals_utid(ut.getUtid());
        Car resultCar = carRepo.save(dto.getCar());
        return new InsertionDTO(resultCar, resultOffer, resultUt);
    }
}
