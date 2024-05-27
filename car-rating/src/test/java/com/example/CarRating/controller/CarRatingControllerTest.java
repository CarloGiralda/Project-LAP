package com.example.CarRating.controller;

import com.example.CarRating.model.CarRating;
import com.example.CarRating.model.CarRatingDTO;
import com.example.CarRating.service.CarRatingService;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = CarRatingController.class)
public class CarRatingControllerTest {
    @MockBean
    CarRatingService carRatingService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnTrueIfTheCarRatingIsSaved() throws Exception {
        // this is the input to convert
        CarRatingDTO dto = new CarRatingDTO(5L, LocalDate.of(2020, 10, 10), "Gentile", "token", 2L);
        dto.setCrid(1L);
        // this is the input converted, and also the body of the response
        CarRating rating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        rating.setCrid(1L);

        ObjectMapper omdto = new ObjectMapper();
        omdto.findAndRegisterModules();
        String dtoAsString = omdto.writeValueAsString(dto);

        when(carRatingService.convertDTO(dto)).thenReturn(rating);

        // create the returned rating
        CarRating expectedRating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        expectedRating.setCrid(1L);
        when(carRatingService.insertRating(ArgumentMatchers.any())).thenReturn(expectedRating);

        mockMvc.perform(MockMvcRequestBuilders.post("/carrating/create")
                        // a custom header must be inserted because of the authentication filter
                        // the username (email) is fake
                        .header("Logged-In-User", "username@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Gentile"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratingOnCar").value(2L));
    }

    @Test
    public void shouldReturnTrueIfACarRatingIsRetrieved() throws Exception {
        // create the returned ratings
        CarRating rating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        rating.setCrid(1L);
        ArrayList<CarRating> ratings = new ArrayList<>();
        ratings.add(rating);

        when(carRatingService.retrieveRatings(rating.getRatingOnCar())).thenReturn(ratings);

        mockMvc.perform(MockMvcRequestBuilders.get("/carrating/{id}", 2L)
                        // a custom header must be inserted because of the authentication filter
                        // the username (email) is fake
                        .header("Logged-In-User", "username@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", containsInAnyOrder("Gentile")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ratingOnCar", containsInAnyOrder(2)));
    }
}
