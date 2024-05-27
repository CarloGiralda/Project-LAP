package com.example.CarSearch.service;

import com.example.CarSearch.model.*;
import com.example.CarSearch.repository.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {
    @Mock
    private SearchRepo searchRepo;
    @Mock
    private CarRepo carRepo;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private DiscoveryClientService discoveryClientService;
    @InjectMocks
    private SearchServiceImpl ssi;

    private static @NotNull SearchDTO getSearchDTO() {
        byte[] image = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic, image);
        car.setCid(1L);
        Offer offer = new Offer(1602288000L, 1602288000L, "10.5", "test@gmail.com", "41.8933203/12.4829321");
        offer.setOid(1L);
        Utilities ut = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut.setUtid(1L);
        return new SearchDTO(car, offer, ut);
    }

    @Test
    @DisplayName("Should pass if a list of searchdto (car, offer and utilities) is returned\n" +
            "First test with the model and the brand assigned to the same brand")
    void shouldReturnTrueIfADTOIsReturned() {
        SearchDTO dto = getSearchDTO();
        // this is the expected result of the repository
        List<SearchDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(dto);

        List<SearchDTO> expectedResponse1 = new ArrayList<>();

        Mockito.when(searchRepo.getCarOfferUtFromSearchDTO(ArgumentMatchers.any(SearchDTO.class))).thenReturn(expectedResponse, expectedResponse1);

        // this is the input
        SearchDTO dto3 = getSearchDTO();
        dto3.getCar().setBrand("Tesla");
        dto3.getCar().setModel("Tesla");

        List<SearchDTO> actualResponse = ssi.getCar(dto3);

        // expectedResponse is also the general expected response of the getCar method
        Assertions.assertEquals(expectedResponse.get(0).getCar().getBrand(), actualResponse.get(0).getCar().getBrand());
        Assertions.assertEquals(expectedResponse.get(0).getCar().getEngine(), actualResponse.get(0).getCar().getEngine());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getPricePerHour(), actualResponse.get(0).getOffer().getPricePerHour());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getFromDate(), actualResponse.get(0).getOffer().getFromDate());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getBluetooth(), actualResponse.get(0).getUtilities().getBluetooth());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getDescription(), actualResponse.get(0).getUtilities().getDescription());
    }

    @Test
    @DisplayName("Should pass if a list of searchdto (car, offer and utilities) is returned\n" +
            "Second test with the model and the brand assigned to the same brand and model")
    void shouldReturnTrueIfADTOIsReturned_SameAsBefore() {
        SearchDTO dto = getSearchDTO();
        // this is the expected result of the repository
        List<SearchDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(dto);

        Mockito.when(searchRepo.getCarOfferUtFromSearchDTO(ArgumentMatchers.any(SearchDTO.class))).thenReturn(expectedResponse);

        // this is the input
        SearchDTO dto3 = getSearchDTO();
        dto3.getCar().setBrand("Tesla S");
        dto.getCar().setModel("Tesla S");
        // similar with "Tesla model S"

        List<SearchDTO> actualResponse = ssi.getCar(dto3);

        // expectedResponse is also the general expected response of the getCar method
        Assertions.assertEquals(expectedResponse.get(0).getCar().getBrand(), actualResponse.get(0).getCar().getBrand());
        Assertions.assertEquals(expectedResponse.get(0).getCar().getEngine(), actualResponse.get(0).getCar().getEngine());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getPricePerHour(), actualResponse.get(0).getOffer().getPricePerHour());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getFromDate(), actualResponse.get(0).getOffer().getFromDate());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getBluetooth(), actualResponse.get(0).getUtilities().getBluetooth());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getDescription(), actualResponse.get(0).getUtilities().getDescription());
    }

    @Test
    @DisplayName("Should pass if a list of searchdto (car, offer and utilities) is returned\n" +
            "First test with the model and the brand assigned to the same brand")
    void shouldReturnTrueIfACarPreviewIsReturned() {
        byte[] image = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic, image);
        car.setCid(1L);

        Mockito.when(carRepo.findCarPreviewById(ArgumentMatchers.any(Long.class))).thenReturn(car);

        // this is the input
        Long input = 1L;

        CarPreviewDTO actualResponse = ssi.getCarPreview(input);

        CarPreviewDTO expectedResponse = new CarPreviewDTO("Tesla", "S", Car.Engine.Electric, 2002L);

        // expectedResponse is also the general expected response of the getCar method
        Assertions.assertEquals(expectedResponse.getBrand(), actualResponse.getBrand());
        Assertions.assertEquals(expectedResponse.getModel(), actualResponse.getModel());
        Assertions.assertEquals(expectedResponse.getEngine(), actualResponse.getEngine());
        Assertions.assertEquals(expectedResponse.getYear(), actualResponse.getYear());
    }

    @Test
    @DisplayName("Should pass if one DTO is returned from its id")
    void shouldReturnADTOFromItsId() {
        Long input_id = 1L;

        SearchDTO dto = getSearchDTO();
        // this is the expected result of the repository
        // this is also the expected response of the method
        List<SearchDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(dto);

        Mockito.when(searchRepo.getCarOfferUtFromId(ArgumentMatchers.any(Long.class))).thenReturn(expectedResponse);

        List<SearchDTO> actualResponse = ssi.getSearchDTOById(input_id);

        Assertions.assertEquals(expectedResponse.get(0).getCar().getBrand(), actualResponse.get(0).getCar().getBrand());
        Assertions.assertEquals(expectedResponse.get(0).getCar().getEngine(), actualResponse.get(0).getCar().getEngine());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getPricePerHour(), actualResponse.get(0).getOffer().getPricePerHour());
        Assertions.assertEquals(expectedResponse.get(0).getOffer().getFromDate(), actualResponse.get(0).getOffer().getFromDate());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getBluetooth(), actualResponse.get(0).getUtilities().getBluetooth());
        Assertions.assertEquals(expectedResponse.get(0).getUtilities().getDescription(), actualResponse.get(0).getUtilities().getDescription());
    }

    @Test
    @DisplayName("Should pass if a string containing both model and brand of a car is returned")
    void shouldReturnAStringContainingModelAndBrandOfACarFromItsId() {
        Long input_id = 1L;

        String response = "Tesla,S";

        Mockito.when(carRepo.findCarById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        String actualResponse = ssi.getCarById(input_id);

        Assertions.assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("Should pass if the availability of the car is returned")
    void shouldReturnTrueIfTheAvailabilityOfTheCarIsReturned() {
        Long input = 1L;

        ResponseEntity<Boolean> response = new ResponseEntity<>(true, HttpStatusCode.valueOf(200));

        Mockito.when(discoveryClientService.getServiceUrl(ArgumentMatchers.eq("CARBOOK-SERVICE"))).thenReturn("http://localhost:9005");
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.eq("http://localhost:9005/reservation/getCarAvailability/" + input), ArgumentMatchers.eq(Boolean.class))).thenReturn(response);

        Boolean actualResponse = ssi.getAvailability(input);
        Boolean expectedResponse = true;

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Should pass if the brand and the model are returned from the car's id")
    void shouldReturnTrueIfTheBrandAndTheModelAreReturnedFromCarsId() {
        Long input = 1L;

        String response = "Tesla,S";

        Mockito.when(carRepo.findCarById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        String actualResponse = ssi.getCarById(input);

        // response is actually the expected response too
        Assertions.assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("Should pass if the renter is returned from the car's id")
    void shouldReturnTrueIfTheRenterIsReturnedFromCarsId() {
        Long input = 1L;

        String response = "renter";

        Mockito.when(carRepo.findCarById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        String actualResponse = ssi.getCarById(input);

        // response is actually the expected response too
        Assertions.assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("Should pass if a list of cars is returned")
    void shouldReturnAllCarsInsideARange() {
        String position = "41.8933203/12.4829321";
        Long range = 1L;

        SearchDTO dto1 = getSearchDTO();

        byte[] image = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Car car2 = new Car("PK555AW", 2020L, Car.PollutionLevel.EURO2, Car.Fuel.Electric, "Fiat", 4L,
                "Cinquecento", Car.Classification.CityCar, 2L, Car.Engine.Electric, Car.Transmission.Automatic, image);
        car2.setCid(1L);
        Offer offer2 = new Offer(1602288000L, 1602288000L, "10.5", "test@gmail.com", "45.4641943/9.1896346");
        offer2.setOid(1L);
        Utilities ut2 = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut2.setUtid(1L);
        SearchDTO dto2 = new SearchDTO(car2, offer2, ut2);

        List<SearchDTO> response = new ArrayList<>();
        response.add(dto1);
        response.add(dto2);

        Mockito.when(searchRepo.getCars()).thenReturn(response);

        JsonArray expectedResponse = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("lat","41.8933203");
        object.addProperty("lon","12.4829321");
        object.addProperty("name","Tesla model S");
        object.addProperty("id",0);
        object.addProperty("cid",1L);

        expectedResponse.add(object);

        JsonArray actualResponse = ssi.getCarsInsideRange(position, range);

        Assertions.assertEquals(expectedResponse.size(), actualResponse.size());
        Assertions.assertEquals(expectedResponse.get(0).getAsJsonObject().get("lat"), actualResponse.get(0).getAsJsonObject().get("lat"));
        Assertions.assertEquals(expectedResponse.get(0).getAsJsonObject().get("lon"), actualResponse.get(0).getAsJsonObject().get("lon"));
        Assertions.assertEquals(expectedResponse.get(0).getAsJsonObject().get("name"), actualResponse.get(0).getAsJsonObject().get("name"));
        Assertions.assertEquals(expectedResponse.get(0).getAsJsonObject().get("id"), actualResponse.get(0).getAsJsonObject().get("id"));
        Assertions.assertEquals(expectedResponse.get(0).getAsJsonObject().get("cid"), actualResponse.get(0).getAsJsonObject().get("cid"));
    }
}