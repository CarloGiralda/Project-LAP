

import com.example.carbook.CarBookApplication;

import com.example.carbook.model.carSubscription.CarSubscriptionRequest;
import com.example.carbook.service.CarSubService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = CarBookApplication .class)
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private CarSubService carSubService;



    @Test
    void subscribeUser() throws Exception {
        mvc.perform(post("http://localhost:9005/subscription/car/{carId}","1").header("Logged-In-User", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void subscribeCarsByUser() throws Exception {
        ArrayList<Long> cars = new ArrayList<>();
        CarSubscriptionRequest c = new CarSubscriptionRequest("1",cars);
        RequestBuilder requestBuilder = post("http://localhost:9005/subscription/subscribeCarsForUser").header("Logged-In-User", "username")
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)).accept(MediaType.TEXT_PLAIN);
        mvc.perform(requestBuilder).andExpect(status().isOk());

    }


    @Test
    void getSubscribedUser() throws Exception {
        ArrayList<String> subscriptions=new ArrayList<>();
        subscriptions.add("sub1");
        when(carSubService.getSubscribedUser(any(String.class))).thenReturn(subscriptions);
        mvc.perform(get("http://localhost:9005/subscription/getSubscribedUser/{carId}","1").header("Logged-In-User", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCarsForUser() throws Exception {
        ArrayList<Long> cars=new ArrayList<>();
        cars.add(1L);
        when(carSubService.getCarsByUser(any(String.class))).thenReturn(cars);
        mvc.perform(get("http://localhost:9005/subscription/getCarsForUser/{userId}","1").header("Logged-In-User", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }







}




