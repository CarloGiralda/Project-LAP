package com.example.CarSearch.controller;

import com.example.CarSearch.model.*;
import com.example.CarSearch.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SearchController.class)
public class SearchControllerTest {
    @MockBean
    private SearchService searchService;
    @Autowired
    private MockMvc mockMvc;

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
    public void shouldReturnTrueIfTheCarIsReturned() throws Exception {
        SearchDTO dto = getSearchDTO();

        ObjectMapper omdto = new ObjectMapper();
        omdto.findAndRegisterModules();
        String dtoAsString = omdto.writeValueAsString(dto);

        List<SearchDTO> list = new ArrayList<>();
        list.add(dto);

        when(searchService.getCar(ArgumentMatchers.any())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/carsearch/searchbar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].cid", containsInAnyOrder(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].brand", containsInAnyOrder("Tesla")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].pricePerHour", containsInAnyOrder("10.5")));
    }

    @Test
    public void shouldReturnTrueListIfNoCarIsFound() throws Exception {
        SearchDTO dto = new SearchDTO();

        ObjectMapper omdto = new ObjectMapper();
        omdto.findAndRegisterModules();
        String dtoAsString = omdto.writeValueAsString(dto);

        ArrayList<SearchDTO> list = new ArrayList<>();

        when(searchService.getCar(ArgumentMatchers.any())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/carsearch/searchbar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
    }

    @Test
    public void shouldReturnTrueIfOneCarIsReturnedFromAnId() throws Exception {
        Long input_id = 1L;

        SearchDTO dto = getSearchDTO();
        List<SearchDTO> dtos = new ArrayList<>();
        dtos.add(dto);

        when(searchService.getSearchDTOById(ArgumentMatchers.any(Long.class))).thenReturn(dtos);
        when(searchService.getAvailability(ArgumentMatchers.any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getCarById/" + input_id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].cid", containsInAnyOrder(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].available", containsInAnyOrder(true)));
    }

    @Test
    public void shouldReturnTrueIfOneCarNameIsReturnedFromAnId() throws Exception {
        Long input_id = 1L;

        String response = "Tesla,S";

        when(searchService.getCarById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getCarNameById/" + input_id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", containsString("Tesla model S")));
    }

    @Test
    public void shouldReturnTrueIfAListOfCarsWithinARangeIsReturned() throws Exception {
        String position = "41.8933203/12.4829321";
        Long range = 10L;

        JsonArray expectedResponse = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("lat",41.8933203);
        object.addProperty("lon",12.4829321);
        object.addProperty("name","Tesla model S");
        object.addProperty("id",0);
        object.addProperty("cid",1L);
        expectedResponse.add(object);

        when(searchService.getCarsInsideRange(ArgumentMatchers.any(String.class), ArgumentMatchers.any(Long.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getCarsWithinRange?position=" + position + "&range=" + range))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].cid", containsInAnyOrder(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Tesla model S")));
    }

    @Test
    public void shouldReturnTrueIfOneCarPreviewIsReturnedFromAnId() throws Exception {
        Long id = 1L;

        CarPreviewDTO dto = new CarPreviewDTO("Tesla", "S", Car.Engine.Electric, 2020L);

        when(searchService.getCarPreview(ArgumentMatchers.any(Long.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getCarPreviewById/" + id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", containsString("Tesla")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2020L));
    }

    @Test
    public void shouldReturnTrueIfOnePriceIsReturnedFromAnId() throws Exception {
        Long id = 1L;

        ArrayList<SearchDTO> response = new ArrayList<>();
        SearchDTO dto = getSearchDTO();
        response.add(dto);

        when(searchService.getSearchDTOById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getPriceById/" + id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(10.5));
    }

    @Test
    public void shouldReturnTrueIfInternalErrorIsReturned() throws Exception {
        Long id = 1L;

        ArrayList<SearchDTO> response = new ArrayList<>();
        SearchDTO dto = getSearchDTO();
        SearchDTO dto2 = getSearchDTO();
        dto2.getCar().setCid(2L);
        dto2.getOffer().setOid(2L);
        dto2.getUtilities().setUtid(2L);
        dto.getOffer().setPricePerHour("22");
        response.add(dto);
        response.add(dto2);

        when(searchService.getSearchDTOById(ArgumentMatchers.any(Long.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getPriceById/" + id))
                .andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    public void shouldReturnTrueIfTheUsernameOfTheRenterIsReturnedFromTheCarId() throws Exception {
        Long id = 1L;

        String renter = "renter";

        when(searchService.getRenter(ArgumentMatchers.any(Long.class))).thenReturn(renter);

        mockMvc.perform(MockMvcRequestBuilders.get("/carsearch/getRenterUsername?id=" + id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.renter", containsString("renter")));
    }
}
