package com.example.carbook.service;

import com.example.carbook.exception.CarSubscriptionException;
import com.example.carbook.exception.UserAlreadySubscribedException;
import com.example.carbook.model.carSubscription.CarSubRepository;
import com.example.carbook.model.carSubscription.CarSubscription;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
@Service
public class CarSubService {

    private CarSubRepository carSubRepository;

    public void subscribeUser(CarSubscription carSubscription) throws Exception {
        try {
            carSubRepository.save(carSubscription);
        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new UserAlreadySubscribedException("user already subscribed");
        }

    }

    public ArrayList<String> getSubscribedUser(String carId){
        Long id = Long.parseLong(carId);
        return carSubRepository.findUserIdByCarId(id);

    }

    public ArrayList<Long> getCarsByUser(String userId) {
        return carSubRepository.findCarsByUser(userId);
    }

    public void subscribeCarsForUserId(String userId,ArrayList<Long> carsId) throws Exception {
        try {
            for(Long car: carsId){
                CarSubscription carSubscription = new CarSubscription(userId,car);


                carSubRepository.save(carSubscription);
            }
        } catch (DataIntegrityViolationException e) {
            log.info("constraints exception");
            throw new Exception("user already subscribed");
        }
    }

    public void deleteSub(Long cid,String username){

        CarSubscription subscription = carSubRepository.findCarSubscriptionByCarIdAndUserId(cid, username);
        if (subscription != null){
            carSubRepository.deleteCarSubscriptionByCarIdAndUserId(cid, username);
        } else {
            throw new CarSubscriptionException("The user is not subscribed for car " + cid);
        }

    }

}
