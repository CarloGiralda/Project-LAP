package com.example.CarRating.service;

import com.example.CarRating.discoveryclient.DiscoveryClientService;
import com.example.CarRating.model.AppUserDTO;
import com.example.CarRating.model.CarRating;
import com.example.CarRating.model.CarRatingDTO;
import com.example.CarRating.repository.CarRatingRepo;
import com.example.CarRating.model.AppUserRoleDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CarRatingServiceTest {
    @Mock
    private CarRatingRepo carRatingRepo;
    @Mock
    private DiscoveryClientService discoveryClientService;
    @Mock
    private RestTemplate restTemplate;


    @Test
    public void shouldReturnTrueIfTheCarRatingIsSaved() {
        CarRatingServiceImpl crsi = new CarRatingServiceImpl(carRatingRepo, discoveryClientService, restTemplate);

        // this is both the input for the 'save' function, and the result of it
        // it is also the expected result
        CarRating rating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        rating.setCrid(1L);

        when(carRatingRepo.save(rating)).thenReturn(rating);

        CarRating actualResponse = crsi.insertRating(rating);

        Assertions.assertEquals(rating.getDate(), actualResponse.getDate());
        Assertions.assertEquals(rating.getStars(), actualResponse.getStars());
    }

    @Test
    public void shouldReturnTrueIfTheConversionGoesWell() {
        CarRatingServiceImpl crsi = new CarRatingServiceImpl(carRatingRepo, discoveryClientService, restTemplate);

        // this is the input to convert
        CarRatingDTO dto = new CarRatingDTO(5L, LocalDate.of(2020, 10, 10), "Gentile", "token", 2L);
        dto.setCrid(1L);

        AppUserDTO audto = new AppUserDTO(1L, "Elia", "Bombardelli", "elia@gmail.com", "Math", AppUserRoleDTO.USER);
        // this is the response of the get method
        ResponseEntity<AppUserDTO> re = new ResponseEntity<>(audto, HttpStatus.OK);

        // this is the expected rating
        CarRating expectedRating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        expectedRating.setCrid(1L);

        when(discoveryClientService.getServiceUrl("USER-SERVICE")).thenReturn("http://localhost:9000");
        when(restTemplate.getForEntity("http://localhost:9000/auth/getUserFromJwt?jwt=" + dto.getMadeByUser(), AppUserDTO.class)).thenReturn(re);

        CarRating actualResponse = crsi.convertDTO(dto);

        Assertions.assertEquals(expectedRating.getStars(), actualResponse.getStars());
        Assertions.assertEquals(expectedRating.getDate(), actualResponse.getDate());
    }

    @Test
    public void shouldReturnTrueIfARatingIsFound() {
        CarRatingServiceImpl crsi = new CarRatingServiceImpl(carRatingRepo, discoveryClientService, restTemplate);

        // this is the input
        Long id = 1L;
        // this is the result of the 'find' function, and the expected ratings
        CarRating rating = new CarRating(5L, LocalDate.of(2020, 10, 10), "Gentile", 1L, 2L);
        rating.setCrid(1L);
        ArrayList<CarRating> ratings = new ArrayList<>();
        ratings.add(rating);

        when(carRatingRepo.findCarRatingByRatingOnCar(id)).thenReturn(ratings);
        ArrayList<CarRating> actualResponse = crsi.retrieveRatings(id);

        Assertions.assertEquals(ratings.get(0).getStars(), actualResponse.get(0).getStars());
        Assertions.assertEquals(ratings.get(0).getDate(), actualResponse.get(0).getDate());
    }
}
