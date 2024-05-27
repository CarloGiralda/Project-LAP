package com.example.demo;

import com.example.demo.controller.PaymentController;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.service.PaymentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInsertTransaction_Success() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("sender","receiver",1L);

        // Mock payment service
        doNothing().when(paymentService).sendTransaction(any(PaymentDTO.class));

        RequestBuilder requestBuilder = post("http://localhost:9010/payment/createTransaction")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDTO)).accept(MediaType.TEXT_PLAIN);

        mvc.perform(requestBuilder).andExpect(status().isOk());


    }

    @Test
    public void testInsertTransaction_Error() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("sender", "receiver", 1L);

        // Mock payment service to throw an exception
        doThrow(new RuntimeException("Something went wrong")).when(paymentService).sendTransaction(any(PaymentDTO.class));

        RequestBuilder requestBuilder = post("http://localhost:9010/payment/createTransaction")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDTO)).accept(MediaType.TEXT_PLAIN);

        mvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testGetBalance_Success() throws Exception {
        String username = "gabriele.lerani2000@gmail.com";

        // Mock payment service
        when(paymentService.getUserBalance(username)).thenReturn(1L);

        RequestBuilder requestBuilder = get("http://localhost:9010/payment/getBalance")
                .header("Logged-In-User", username);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1L)));;


    }

    @Test
    public void testGetBalance_Error() throws Exception {
        String username = "gabriele.lerani2000@gmail.com";

        // Mock payment service
        when(paymentService.getUserBalance(anyString())).thenThrow(new RuntimeException("Internal Server Error"));

        RequestBuilder requestBuilder = get("http://localhost:9010/payment/getBalance")
                .header("Logged-In-User", username);

        mvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());


    }


}
