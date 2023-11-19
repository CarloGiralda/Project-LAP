package com.example.CarInsertion.service;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.repository.CarRepo;
import com.example.CarInsertion.repository.OfferRepo;
import com.example.CarInsertion.repository.UtilitiesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

public class InsertionServiceTest {
    @Mock
    private CarRepo carRepo;
    @Mock
    private OfferRepo offerRepo;
    @Mock
    private UtilitiesRepo utilitiesRepo;
    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;
    @Captor
    private ArgumentCaptor<Offer> offerArgumentCaptor;
    @Captor
    private ArgumentCaptor<Utilities> utilitiesArgumentCaptor;

    @ExtendWith(MockitoExtension.class)
    @Test
    @DisplayName("Should pass if a list of cars, offers and utilities is returned")
    void shouldInsertTheInputIntoTheDatabase() {
        InsertionServiceImpl isi = new InsertionServiceImpl(carRepo, offerRepo, utilitiesRepo);

        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);

        Offer offer = new Offer(LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10), "10.5");

        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");

        InsertionDTO inputDTO = new InsertionDTO(car, offer, ut);

        when(carRepo.save(ArgumentMatchers.any(Car.class))).thenReturn(car);
        when(offerRepo.save(ArgumentMatchers.any(Offer.class))).thenReturn(offer);
        when(utilitiesRepo.save(ArgumentMatchers.any(Utilities.class))).thenReturn(ut);

        InsertionDTO expectedResponse = new InsertionDTO(car, offer, ut);

        InsertionDTO actualResponse = isi.insert(inputDTO);

        Assertions.assertEquals(expectedResponse.getCar().getBrand(), actualResponse.getCar().getBrand());
        Assertions.assertEquals(expectedResponse.getCar().getEngine(), actualResponse.getCar().getEngine());
        Assertions.assertEquals(expectedResponse.getOffer().getPricePerDay(), actualResponse.getOffer().getPricePerDay());
        Assertions.assertEquals(expectedResponse.getOffer().getFromDate(), actualResponse.getOffer().getFromDate());
        Assertions.assertEquals(expectedResponse.getUtilities().getBluetooth(), actualResponse.getUtilities().getBluetooth());
        Assertions.assertEquals(expectedResponse.getUtilities().getDescription(), actualResponse.getUtilities().getDescription());
        Mockito.verify(carRepo, Mockito.times(1)).save(carArgumentCaptor.capture());
        Mockito.verify(offerRepo, Mockito.times(1)).save(offerArgumentCaptor.capture());
        Mockito.verify(utilitiesRepo, Mockito.times(1)).save(utilitiesArgumentCaptor.capture());
    }
}
