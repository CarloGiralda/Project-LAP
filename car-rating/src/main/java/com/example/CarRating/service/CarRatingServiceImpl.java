package com.example.CarRating.service;

import com.example.CarRating.exception.RatingAlreadyExists;
import com.example.CarRating.model.AppUserDTO;
import com.example.CarRating.discoveryclient.DiscoveryClientService;
import com.example.CarRating.model.CarRating;
import com.example.CarRating.model.CarRatingDTO;
import com.example.CarRating.repository.CarRatingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Slf4j
@Service
public class CarRatingServiceImpl implements CarRatingService {
    private final CarRatingRepo carRatingRepo;
    private final DiscoveryClientService discoveryClientService;
    private final RestTemplate restTemplate;

    public CarRatingServiceImpl(CarRatingRepo carRatingRepo, DiscoveryClientService discoveryClientService, RestTemplate restTemplate) {
        this.carRatingRepo = carRatingRepo;
        this.discoveryClientService = discoveryClientService;
        this.restTemplate = restTemplate;
    }

    @Override
    public CarRating insertRating(CarRating carRating) {
        try {
            // checks about user id and renter id are performed in the convertDTO function
            // that is called before this function
            return carRatingRepo.save(carRating);
        } catch (DataIntegrityViolationException e) {

            log.error("Error occurred while inserting rating: {}", e.getMessage());
            // Or return a specific response:
            throw new RatingAlreadyExists("Duplicate key violation occurred: " + e.getMessage());
        }

    }

    @Override
    public CarRating convertDTO(CarRatingDTO dto) {
        String requestForUser = dto.getMadeByUser();
        String userJwtUrl = discoveryClientService.getServiceUrl("USER-SERVICE") + "/auth/getUserFromJwt?jwt=" + requestForUser;

        // Send the request to the user service
        ResponseEntity<AppUserDTO> user = restTemplate.getForEntity(userJwtUrl, AppUserDTO.class);

        return new CarRating(dto.getStars(), dto.getDate(), dto.getDescription(), user.getBody().getId(), dto.getRatingOnCar());
    }
    @Override
    public ArrayList<CarRating> retrieveRatings(Long id) {
        return carRatingRepo.findCarRatingByRatingOnCar(id);
    }
}
