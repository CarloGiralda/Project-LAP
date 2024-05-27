package com.example.CarInsertion.controller;

import com.example.CarInsertion.model.*;
import com.example.CarInsertion.service.InsertionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = InsertionController.class)
public class InsertionControllerTest {
    @MockBean
    private InsertionService insertionService;
    @Autowired
    private MockMvc mockMvc;

    private InsertionDTO getInsertionDTO() {
        byte[] inputArray = "Test String".getBytes();

        Offer offer = new Offer(1602288000L, 1602288000L, "10.5", "test@gmail.com", "Roma");
        offer.setOid(1L);
        Car car = new Car("PK543WQ", 2002L, Car.PollutionLevel.EURO4, Car.Fuel.Methane, "Tesla", 4L,
                "S", Car.Classification.CityCar, 4L, Car.Engine.Electric, Car.Transmission.Automatic, inputArray);
        car.setCid(1L);
        Utilities ut = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut.setUtid(1L);

        return new InsertionDTO(car, offer, ut);
    }

    @Test
    public void shouldReturnTrueIfTheOfferIsStored() throws Exception {
        // create an offer object to pass it to the page
        Offer offer = new Offer(1602288000L, 1602288000L, "10.5", "test@gmail.com", "Roma");

        // convert it into an object that is accepted by subsequent operations
        ObjectMapper omof = new ObjectMapper();
        omof.findAndRegisterModules();
        String offerAsString = omof.writeValueAsString(offer);

        // test if the controller works fine if an input object is provided
        mockMvc.perform(MockMvcRequestBuilders.post("/carinsert/insertoffer")
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(offerAsString))
                .andExpect(MockMvcResultMatchers.status().is(200));

        // create a malformed offer object to pass it to the page
        Offer malformedOffer = new Offer();
        malformedOffer.setFromDate(1602288000L);
        malformedOffer.setToDate(1602288000L);

        // convert it into an object that is accepted by subsequent operations
        String malformedOfferAsString = omof.writeValueAsString(malformedOffer);

        // test if the controller works fine if a malformed object is provided
        mockMvc.perform(MockMvcRequestBuilders.post("/carinsert/insertoffer")
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedOfferAsString))
                .andExpect(MockMvcResultMatchers.status().is(412));
    }

    @Test
    public void shouldReturnTrueIfTheCarIsStored() throws Exception {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", inputArray);

        // test if the controller works fine if an input object is provided
        mockMvc.perform(MockMvcRequestBuilders.multipart("/carinsert/insertcar")
                        .file(mockMultipartFile)
                        .param("plateNum", "PK543WQ")
                        .param("year", "2002")
                        .param("pollutionLevel", String.valueOf(Car.PollutionLevel.EURO4))
                        .param("fuel", String.valueOf(Car.Fuel.Methane))
                        .param("brand", "Tesla")
                        .param("passengers", "4")
                        .param("model", "S")
                        .param("classification", String.valueOf(Car.Classification.CityCar))
                        .param("carDoorNumber", "4")
                        .param("engine", String.valueOf(Car.Engine.Electric))
                        .param("transmission", String.valueOf(Car.Transmission.Automatic))
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200));

        // create a malformed car object to pass it to the page
        mockMvc.perform(MockMvcRequestBuilders.multipart("/carinsert/insertcar")
                        .file(mockMultipartFile)
                        .param("plateNum", "PK543WQ")
                        .param("year", "2002")
                        .param("pollutionLevel", String.valueOf(Car.PollutionLevel.EURO4))
                        .param("brand", "Tesla")
                        .param("passengers", "4")
                        .param("model", "S")
                        .param("carDoorNumber", "4")
                        .param("engine", String.valueOf(Car.Engine.Electric))
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(412));
    }

    @Test
    public void shouldReturnTrueIfAllDetailsOfTheCarAreSaved() throws Exception {
        // create a utilities object to pass it to the page
        Utilities ut = new Utilities(true, true, true, true, true, false,
                true, true, true, true, false, true, true,
                true, "BELLA");
        ut.setUtid(1L);

        InsertionDTO expectedResponse = getInsertionDTO();

        // convert it into an object that is accepted by subsequent operations
        ObjectMapper omut = new ObjectMapper();
        omut.findAndRegisterModules();
        String utAsString = omut.writeValueAsString(ut);

        when(insertionService.insert(ArgumentMatchers.any(InsertionDTO.class))).thenReturn(expectedResponse);

        // test if the controller works fine if an input object is provided
        mockMvc.perform(MockMvcRequestBuilders.post("/carinsert/insertutilities")
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utAsString))
                .andExpect(MockMvcResultMatchers.status().is(200));

        // create a malformed utilities object to pass it to the page
        Utilities malformedUt = new Utilities();
        malformedUt.setAirConditioning(true);
        malformedUt.setCdPlayer(true);

        // convert it into an object that is accepted by subsequent operations
        String malformedUtAsString = omut.writeValueAsString(malformedUt);

        // test if the controller works fine if a malformed object is provided
        mockMvc.perform(MockMvcRequestBuilders.post("/carinsert/insertutilities")
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedUtAsString))
                .andExpect(MockMvcResultMatchers.status().is(412));
    }

    @Test
    public void shouldReturnTrueIfAListOfCarsIsReturned() throws Exception {
        String input = "test";

        InsertionDTO dto = getInsertionDTO();
        List<InsertionDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(dto);

        when(insertionService.retrieveCars(ArgumentMatchers.any(String.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/carinsert/listcars?user=" + input)
                        .header("Logged-In-User", "test@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.isA(ArrayList.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*][*].cid", containsInAnyOrder(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*][*].zoneLocation", containsInAnyOrder("Roma")));
    }

    @Test
    public void shouldReturnTrueIfTheCarIsUpdated() throws Exception {
        Long input = 1L;

        InsertionDTO response = getInsertionDTO();

        // convert it into an object that is accepted by subsequent operations
        ObjectMapper omdto = new ObjectMapper();
        omdto.findAndRegisterModules();
        String dtoAsString = omdto.writeValueAsString(response);

        when(insertionService.update(ArgumentMatchers.any(InsertionDTO.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/carinsert/modify/" + input)
                        .header("Logged-In-User", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
