package com.example.userservice.controller;

import com.example.userservice.appuser.service.AppUserService;
import com.example.userservice.exception.ConfirmationTokenExpiredException;
import com.example.userservice.exception.ConfirmationTokenNotFoundException;
import com.example.userservice.exception.EmailAlreadyConfirmedException;
import com.example.userservice.registration.registrationtoken.ConfirmationTokenController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConfirmationTokenController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    public static final String testEndPoint = "/confirmation/confirm";

    @Test
    public void ConfirmationToken_confirmToken_returnValidated() throws Exception{
        when(appUserService.confirmRegistrationToken(any(String.class)))
                .thenReturn(true);

        mockMvc.perform(get(testEndPoint)
                        .param("token","test-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration token validated"))
                .andDo(print());


    }

    @Test
    public void ConfirmationToken_confirmToken_returnBadRequest() throws Exception{
        when(appUserService.confirmRegistrationToken(any(String.class)))
                .thenReturn(false);

        mockMvc.perform(get(testEndPoint)
                        .param("token","test-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration token not valid"))
                .andDo(print());


    }

    @Test
    public void confirm_WhenTokenNotFound_ReturnsNotFound() throws Exception {

        String nonExistentToken = "nonExistentToken";
        when(appUserService.confirmRegistrationToken(nonExistentToken))
                .thenThrow(new ConfirmationTokenNotFoundException("Token not found"));


        mockMvc.perform(get(testEndPoint).param("token", nonExistentToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Token not found"))
                .andDo(print());
    }

    @Test
    void confirm_WhenEmailAlreadyConfirmed_ReturnsBadRequest() throws Exception {

        String token = "someToken";
        when(appUserService.confirmRegistrationToken(token))
                .thenThrow(new EmailAlreadyConfirmedException("Email already confirmed"));


        mockMvc.perform(get(testEndPoint).param("token", token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already confirmed"))
                .andDo(print());
    }

    @Test
    void confirm_WhenTokenExpired_ReturnsBadRequest() throws Exception {

        String expiredToken = "expiredToken";
        when(appUserService.confirmRegistrationToken(expiredToken))
                .thenThrow(new ConfirmationTokenExpiredException("Token expired"));


        mockMvc.perform(get(testEndPoint).param("token", expiredToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Token expired"))
                .andDo(print());
    }


}
