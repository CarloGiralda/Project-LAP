package com.example.CarInsertion.controller;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.service.InsertionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.core.StringContains.containsString;

@WebMvcTest(controllers = InsertionController.class)
public class InsertionControllerTest {
    @MockBean
    private InsertionService insertionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOfferForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/insertoffer"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Offer Insertion Form")));
    }

    @Test
    public void shouldReturnCarForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/insertcar"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Car Insertion Form")));
    }

    @Test
    public void shouldReturnUtilitiesForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/insertutilities"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Utilities Insertion Form")));
    }

    @Test
    public void shouldSaveOffer() throws Exception {
        Offer offer = new Offer(LocalDate.of(2020,10,10), LocalDate.of(2020,10,10), "10.5");

        ObjectMapper omof = new ObjectMapper();
        omof.findAndRegisterModules();
        String offerAsString = omof.writeValueAsString(offer);

        mockMvc.perform(MockMvcRequestBuilders.post("/insertoffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(offerAsString))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }

    @Test
    public void shouldSaveCar() throws Exception {
        Car car = new Car("PK543WQ", 2002L, "EURO4", "Benzina", "Tesla", 4L,
                "S", "nothing", "Roma", Car.Size.Small, 4L, Car.Engine.Electric, Car.Transmission.Automatic);

        ObjectMapper omcar = new ObjectMapper();
        omcar.findAndRegisterModules();
        String carAsString = omcar.writeValueAsString(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/insertoffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carAsString))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }

    @Test
    public void shouldSaveUtilities() throws Exception {
        Utilities ut = new Utilities(Utilities.Assistant.Android, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");

        ObjectMapper omut = new ObjectMapper();
        omut.findAndRegisterModules();
        String utAsString = omut.writeValueAsString(ut);

        mockMvc.perform(MockMvcRequestBuilders.post("/insertoffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utAsString))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }
}
