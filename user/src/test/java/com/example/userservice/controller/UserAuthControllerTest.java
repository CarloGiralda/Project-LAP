package com.example.userservice.controller;

import com.example.userservice.appuser.controller.UserAuthController;
import com.example.userservice.appuser.dto.LoginRequest;
import com.example.userservice.appuser.service.AppUserService;
import com.example.userservice.registration.dto.RegistrationRequest;
import com.example.userservice.registration.service.RegistrationFieldValidator;
import com.example.userservice.registration.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UserAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RegistrationFieldValidator registrationFieldValidator;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    private RegistrationRequest requestDto;
    private LoginRequest loginDto;

    private static final String authPrefix = "/auth";

    @BeforeEach
    public void init(){
        // set value used for all tests
        requestDto = new RegistrationRequest("test-admin", "test-admin", "test-admin@gmail.com","password");
        loginDto = new LoginRequest("test-admin@gmail.com","password");
    }

    @Test
    public void UserAuthController_createUser_returnCreated() throws Exception {

        Long mockedId = 1L;

        when(registrationService.register(any(RegistrationRequest.class)))
                .thenReturn(mockedId);
        when(registrationFieldValidator.testEmail(requestDto.getEmail())).thenReturn(true);
        when(registrationFieldValidator.testName(requestDto.getFirstName())).thenReturn(true);
        when(registrationFieldValidator.testName(requestDto.getLastName())).thenReturn(true);



        mockMvc.perform(post(authPrefix+"/register")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    public void UserAuthController_getJwtToken_returnBadRequest() throws Exception{


        // Mock the authenticationManager to return a successfully authenticated token
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication());

        // Mock the appUserService method to return a token
        when(appUserService.generateJwtToken(any(String.class)))
                .thenReturn("mocked-token");


        // Perform the getToken request using MockMvc
        mockMvc.perform(post(authPrefix+"/token")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void UserAuthController_validateJwtToken_returnBadRequest() throws Exception{
        String wrongToken = "1234";

        when(appUserService.validateJwtToken(wrongToken)).thenReturn(null);

        mockMvc.perform(get(authPrefix+"/validate")
                        .param("token",wrongToken)
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    private Authentication mockAuthentication() {
        // You can create a mock Authentication object for a successfully authenticated user
        return new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
    }

    @Test
    public void UserAuthController_extractUsername_returnUsername() throws Exception{

        String token = "1234";
        String mockedUsername = "mocked-username";

        when(appUserService.extractUsernameFromJwtToken(any(String.class))).thenReturn(mockedUsername);

        mockMvc.perform(get(authPrefix+"/extractUsernameFromJwt").param("token", token))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    public void UserAuthController_extractUsername_returnInternalError() throws Exception{

        String token = "1234";
        when(appUserService.extractUsernameFromJwtToken(any(String.class)))
                .thenThrow(new RuntimeException("username extraction failed"));

        mockMvc.perform(get(authPrefix+"/extractUsernameFromJwt").param("token", token))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("cannot extract username from jwt"))
                .andDo(print());

    }
}
