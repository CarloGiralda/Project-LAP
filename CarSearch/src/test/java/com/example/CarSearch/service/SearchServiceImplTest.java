package com.example.CarSearch.service;

import com.example.CarSearch.model.*;
import com.example.CarSearch.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.Specification.where;

class SearchServiceImplTest {

    @Mock
    private CarRepo carRepo;
    @Mock
    private OfferRepo offerRepo;
    @Mock
    private UtilitiesRepo utilitiesRepo;
    @Captor
    private ArgumentCaptor<Specification<Car>> specificationCarArgumentCaptor;
    @Captor
    private ArgumentCaptor<Specification<Offer>> specificationOfferArgumentCaptor;
    @Captor
    private ArgumentCaptor<Specification<Utilities>> specificationUtilitiesArgumentCaptor;

    @ExtendWith(MockitoExtension.class)
    @Test
    @DisplayName("Should pass if a list of cars, offers and utilities is returned")
    void shouldReturnACustomDTO() {
        SearchServiceImpl ssi = new SearchServiceImpl(carRepo, offerRepo, utilitiesRepo);

        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);
        List<Car> cars = new ArrayList<Car>();
        cars.add(car);
        /*
        Mockito.when(carRepo.findAll(where(CarSpecifications.equalYear(2023L))
                .and(CarSpecifications.equalPollLvl("EURO4"))
                .and(CarSpecifications.equalFuel("Benzina"))
                .and(CarSpecifications.equalBrand("Tesla"))
                .and(CarSpecifications.equalPassengers(4L))
                .and(CarSpecifications.likeModel("S"))
                .and(CarSpecifications.equalPosition("Roma"))
                .and(CarSpecifications.equalSize(Car.Size.Small))
                .and(CarSpecifications.equalDoor(4L))
                .and(CarSpecifications.equalEngine(Car.Engine.Electric))
                .and(CarSpecifications.equalTransmission(Car.Transmission.Automatic)))).thenReturn(cars);
        */
        Mockito.when(carRepo.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(cars);

        Offer offer = new Offer(LocalDate.of(2020,10,10), LocalDate.of(2020,10,10), "10.5");
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(offer);
        /*
        Mockito.when(offerRepo.findAll(where(OfferSpecifications.equalAvailable(true))
                .and(OfferSpecifications.equalFromDate(LocalDate.of(2020,10,10)))
                .and(OfferSpecifications.equalToDate(LocalDate.of(2020,10,10)))
                .and(OfferSpecifications.equalPricePerDay("10.5")))).thenReturn(offers);
         */
        Mockito.when(offerRepo.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(offers);

        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        List<Utilities> uts = new ArrayList<Utilities>();
        uts.add(ut);
        /*
        Mockito.when(utilitiesRepo.findAll(where(UtilitiesSpecifications.equalDisplay(true))
                .and(UtilitiesSpecifications.equalAssistant(Utilities.Assistant.Android))
                .and(UtilitiesSpecifications.equalAirConditioning(true))
                .and(UtilitiesSpecifications.equalStartAndStop(true))
                .and(UtilitiesSpecifications.equalNavigationSystem(false))
                .and(UtilitiesSpecifications.equalParkingAssistant(true))
                .and(UtilitiesSpecifications.equalBluetooth(true))
                .and(UtilitiesSpecifications.equalUSBPorts(true))
                .and(UtilitiesSpecifications.equalCDPlayer(true))
                .and(UtilitiesSpecifications.equalRadioAMFM(false))
                .and(UtilitiesSpecifications.equalCruiseControl(true))
                .and(UtilitiesSpecifications.equalParkingCamera(true))
                .and(UtilitiesSpecifications.equalSurroundAudio(true)))).thenReturn(uts);
         */
        Mockito.when(utilitiesRepo.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(uts);

        ResponseDTO expectedResponse = new ResponseDTO(cars, offers, uts);

        SearchDTO dto = new SearchDTO(car, offer, ut);
        ResponseDTO actualResponse = ssi.getCar(dto);

        Assertions.assertEquals(expectedResponse.getCars().get(0).getBrand(), actualResponse.getCars().get(0).getBrand());
        Assertions.assertEquals(expectedResponse.getCars().get(0).getEngine(), actualResponse.getCars().get(0).getEngine());
        Assertions.assertEquals(expectedResponse.getOffers().get(0).getPricePerDay(), actualResponse.getOffers().get(0).getPricePerDay());
        Assertions.assertEquals(expectedResponse.getOffers().get(0).getFromDate(), actualResponse.getOffers().get(0).getFromDate());
        Assertions.assertEquals(expectedResponse.getUtilities().get(0).getBluetooth(), actualResponse.getUtilities().get(0).getBluetooth());
        Assertions.assertEquals(expectedResponse.getUtilities().get(0).getDescription(), actualResponse.getUtilities().get(0).getDescription());
        Mockito.verify(carRepo, Mockito.times(1)).findAll(specificationCarArgumentCaptor.capture());
        Mockito.verify(offerRepo, Mockito.times(1)).findAll(specificationOfferArgumentCaptor.capture());
        Mockito.verify(utilitiesRepo, Mockito.times(1)).findAll(specificationUtilitiesArgumentCaptor.capture());
    }
}