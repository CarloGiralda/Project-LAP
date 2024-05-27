package com.example.carbook.controller;


import com.example.carbook.exception.CarSubscriptionException;
import com.example.carbook.exception.UserAlreadySubscribedException;
import com.example.carbook.model.carSubscription.CarSubscription;
import com.example.carbook.model.carSubscription.CarSubscriptionRequest;
import com.example.carbook.service.CarSubService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/subscription")
@RestController
public class CarSubController {

    private CarSubService carSubService;

    @PostMapping(path = "/car/{carId}")
    @ResponseBody
    public ResponseEntity<?> subscribeUser(@RequestHeader("Logged-In-User") String username, @PathVariable String carId) throws Exception {

        try {
            CarSubscription carSubscription = new CarSubscription(username, Long.parseLong(carId));
            carSubService.subscribeUser(carSubscription);
            return new ResponseEntity<>("user subscribed",HttpStatus.OK);
        } catch (UserAlreadySubscribedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(path = "/subscribeCarsForUser")
    @ResponseBody
    public ResponseEntity<?> subscribeCarsByUser(@RequestBody CarSubscriptionRequest carSubscriptionRequest) throws Exception {
        try {
            log.info("REQUEST FROM AREA SERVICE: "+carSubscriptionRequest);
            carSubService.subscribeCarsForUserId(carSubscriptionRequest.getUserId(), carSubscriptionRequest.getCarsId());
            return new ResponseEntity<>("cars subscribed for user " + carSubscriptionRequest.getUserId(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "getSubscribedUser/{carId}")
    public ResponseEntity<?> getSubscribedUser(@PathVariable String carId) {

        try {
            ArrayList<String> subscriptions = carSubService.getSubscribedUser(carId);
            if(!subscriptions.isEmpty()) {
                return new ResponseEntity<>(subscriptions, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cannot find user subscribed for car " + carId);
            }

        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal error");
        }
    }


    @GetMapping(path = "getCarsForUser/{userId}")
    public ResponseEntity<?> getCarsForUser(@PathVariable String userId) {

        try {
            ArrayList<Long> cars = carSubService.getCarsByUser(userId);
            if(!cars.isEmpty()) {
                return new ResponseEntity<>(cars, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cannot find cars for that user");
            }

        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal error");
        }
    }


    @DeleteMapping(path = "deleteSub/{carId}")
    public ResponseEntity<?> deleteSubscription(@RequestHeader("Logged-In-User") String username, @PathVariable Long carId) {

        try {
            carSubService.deleteSub(carId, username);
            return new ResponseEntity<>("User removed", HttpStatus.OK);

        } catch (CarSubscriptionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal error");

        }
    }


}
