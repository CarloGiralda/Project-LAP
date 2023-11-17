package com.example.CarSearch.service;

import com.example.CarSearch.model.*;
import com.example.CarSearch.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class SearchServiceImpl implements SearchService {
    private final CarRepo carRepo;
    private final OfferRepo offerRepo;
    private final UtilitiesRepo utilitiesRepo;

    @Autowired
    public SearchServiceImpl(CarRepo carRepo, OfferRepo offerRepo, UtilitiesRepo utilitiesRepo) {
        super();
        this.carRepo = carRepo;
        this.offerRepo = offerRepo;
        this.utilitiesRepo = utilitiesRepo;
    }

    @Override
    @ResponseBody
    public ResponseDTO getCar(@RequestBody SearchDTO dto) {
        List<Car> cars = carRepo.findAll(where(CarSpecifications.equalYear(dto.getCar().getYear()))
                .and(CarSpecifications.equalPollLvl(dto.getCar().getPollutionLevel()))
                .and(CarSpecifications.equalFuel(dto.getCar().getFuel()))
                .and(CarSpecifications.equalBrand(dto.getCar().getBrand()))
                .and(CarSpecifications.equalPassengers(dto.getCar().getPassengers()))
                .and(CarSpecifications.likeModel(dto.getCar().getModel()))
                .and(CarSpecifications.equalPosition(dto.getCar().getPosition()))
                .and(CarSpecifications.equalSize(dto.getCar().getSize()))
                .and(CarSpecifications.equalDoor(dto.getCar().getCarDoorNumber()))
                .and(CarSpecifications.equalEngine(dto.getCar().getEngine()))
                .and(CarSpecifications.equalTransmission(dto.getCar().getTransmission())));

        List<Offer> offers = offerRepo.findAll(where(OfferSpecifications.equalAvailable(dto.getOffer().isAvailable()))
                .and(OfferSpecifications.equalFromDate(dto.getOffer().getFromDate()))
                .and(OfferSpecifications.equalToDate(dto.getOffer().getToDate()))
                .and(OfferSpecifications.equalPricePerDay(dto.getOffer().getPricePerDay())));

        List<Utilities> utilities = utilitiesRepo.findAll(where(UtilitiesSpecifications.equalDisplay(dto.getUtilities().getDisplay()))
                .and(UtilitiesSpecifications.equalAssistant(dto.getUtilities().getAssistant()))
                .and(UtilitiesSpecifications.equalAirConditioning(dto.getUtilities().getAirConditioning()))
                .and(UtilitiesSpecifications.equalStartAndStop(dto.getUtilities().getStartAndStop()))
                .and(UtilitiesSpecifications.equalNavigationSystem(dto.getUtilities().getNavigationSystem()))
                .and(UtilitiesSpecifications.equalParkingAssistant(dto.getUtilities().getParkingAssistant()))
                .and(UtilitiesSpecifications.equalBluetooth(dto.getUtilities().getBluetooth()))
                .and(UtilitiesSpecifications.equalUSBPorts(dto.getUtilities().getUSBPorts()))
                .and(UtilitiesSpecifications.equalCDPlayer(dto.getUtilities().getCDPlayer()))
                .and(UtilitiesSpecifications.equalRadioAMFM(dto.getUtilities().getRadioAMFM()))
                .and(UtilitiesSpecifications.equalCruiseControl(dto.getUtilities().getCruiseControl()))
                .and(UtilitiesSpecifications.equalParkingCamera(dto.getUtilities().getParkingCamera()))
                .and(UtilitiesSpecifications.equalSurroundAudio(dto.getUtilities().getSurroundAudio())));

        return new ResponseDTO(cars, offers, utilities);
    }
}
