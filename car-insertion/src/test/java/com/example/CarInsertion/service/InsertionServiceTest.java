package com.example.CarInsertion.service;

import com.example.CarInsertion.discoveryclient.DiscoveryClientService;
import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class InsertionServiceTest {
    @Mock
    private CarRepo carRepo;
    @Mock
    private OfferRepo offerRepo;
    @Mock
    private AuctionRepo auctionRepo;
    @Mock
    private UtilitiesRepo utilitiesRepo;
    @Mock
    private CarOfferUtRepo carOfferUtRepo;

    @Mock
    private DiscoveryClientService discoveryClientService;

    @Mock
    private RestTemplate restTemplate;

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
        InsertionServiceImpl isi = new InsertionServiceImpl(carRepo, offerRepo, auctionRepo, utilitiesRepo, carOfferUtRepo, discoveryClientService, restTemplate);

        byte[] image = "Test String".getBytes();

        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic, image);

        Offer offer = new Offer(1602288000L, 1602288000L, "10.5", "test@gmail.com", "Roma");

        Utilities ut = new Utilities(true, true, true, true, true, false,
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
        Assertions.assertEquals(expectedResponse.getOffer().getPricePerHour(), actualResponse.getOffer().getPricePerHour());
        Assertions.assertEquals(expectedResponse.getOffer().getFromDate(), actualResponse.getOffer().getFromDate());
        Assertions.assertEquals(expectedResponse.getUtilities().getBluetooth(), actualResponse.getUtilities().getBluetooth());
        Assertions.assertEquals(expectedResponse.getUtilities().getDescription(), actualResponse.getUtilities().getDescription());
        Mockito.verify(carRepo, Mockito.times(1)).save(carArgumentCaptor.capture());
        Mockito.verify(offerRepo, Mockito.times(1)).save(offerArgumentCaptor.capture());
        Mockito.verify(utilitiesRepo, Mockito.times(1)).save(utilitiesArgumentCaptor.capture());
    }

    @ExtendWith(MockitoExtension.class)
    @Test
    @DisplayName("Should pass if a list of cars, offers and utilities is returned")
    void shouldUpdateTheDatabase() {
        InsertionServiceImpl isi = new InsertionServiceImpl(carRepo, offerRepo, auctionRepo, utilitiesRepo, carOfferUtRepo, discoveryClientService, restTemplate);

        byte[] image = "Test String".getBytes();

        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic);
        car.setCid(1L);

        Offer offer = new Offer(1602288000L, 1602288000L,
                "10.5", "test@gmail.com", "Roma");
        offer.setOid(1L);

        Utilities ut = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut.setUtid(1L);

        InsertionDTO inputDTO = new InsertionDTO(car, offer, ut);

        // test if, if each table of the database is updated, the service returns 1
        when(carOfferUtRepo.updateCar(ArgumentMatchers.any(Car.class))).thenReturn(1);
        when(carOfferUtRepo.updateOffer(ArgumentMatchers.any(Offer.class))).thenReturn(1);
        when(carOfferUtRepo.updateUtilities(ArgumentMatchers.any(Utilities.class))).thenReturn(1);
        when(carRepo.findOfferOid(ArgumentMatchers.any(Long.class))).thenReturn(offer);
        when(carRepo.findUtilitiesUtid(ArgumentMatchers.any(Long.class))).thenReturn(ut);

        int expectedResponse = 1;

        int result = isi.update(inputDTO);

        Assertions.assertEquals(result, expectedResponse);
    }

    @ExtendWith(MockitoExtension.class)
    @Test
    @DisplayName("Should pass if a list of cars, offers and utilities is returned")
    void shouldRetrieveCarsFromAnUsername() {
        InsertionServiceImpl isi = new InsertionServiceImpl(carRepo, offerRepo, auctionRepo, utilitiesRepo, carOfferUtRepo, discoveryClientService, restTemplate);

        String input_username = "test@gmail.com";

        byte[] image = "Test String".getBytes();

        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic, image);
        car.setCid(1L);
        Offer offer = new Offer(1602288000L, 1602288000L,
                "10.5", "test@gmail.com", "Roma");
        offer.setOid(1L);
        Utilities ut = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut.setUtid(1L);

        InsertionDTO dto = new InsertionDTO(car, offer, ut);
        List<InsertionDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(dto);

        when(carOfferUtRepo.getCarOfferUtFromUsername(input_username)).thenReturn(expectedResponse);

        List<InsertionDTO> actualResponse = isi.retrieveCars(input_username);

        Assertions.assertEquals(actualResponse, expectedResponse);
    }
}
