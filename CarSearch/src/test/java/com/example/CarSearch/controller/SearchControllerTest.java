package com.example.CarSearch.controller;

import com.example.CarSearch.model.*;
import com.example.CarSearch.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SearchController.class)
public class SearchControllerTest {
    @MockBean
    private SearchService searchService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Search A Car")));
    }

    @Test
    public void shouldPostCar() throws Exception {

        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);
        Offer offer = new Offer(LocalDate.of(2020,10,10), LocalDate.of(2020,10,10), "10.5");
        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        SearchDTO dto = new SearchDTO(car, offer, ut);

        ObjectMapper omdto = new ObjectMapper();
        omdto.findAndRegisterModules();
        String dtoAsString = omdto.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }

    @Test
    public void shouldListCars() throws Exception {
        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);
        List<Car> cars = new ArrayList<Car>();
        cars.add(car);
        Offer offer = new Offer(LocalDate.of(2020,10,10), LocalDate.of(2020,10,10), "10.5");
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(offer);
        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        List<Utilities> uts = new ArrayList<Utilities>();
        uts.add(ut);
        ResponseDTO response = new ResponseDTO(cars, offers, uts);

        when(searchService.getCar(ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("List Cars")));
    }
}
