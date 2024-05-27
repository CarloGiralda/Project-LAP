package com.example.carbook.model.carSubscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public interface CarSubRepository extends JpaRepository<CarSubscription, Integer> {


    @Query("SELECT c.userId from CarSubscription c WHERE c.carId = ?1")
    ArrayList<String> findUserIdByCarId(Long carId);

    @Query("SELECT c.carId from CarSubscription c WHERE c.userId = ?1")
    ArrayList<Long> findCarsByUser(String userId);


    CarSubscription findCarSubscriptionByCarIdAndUserId(Long cid, String username);

    @Transactional
    void deleteCarSubscriptionByCarIdAndUserId(Long cid, String username);



}
