package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<Car,Long> {
    @Query("SELECT c.offer_oid FROM Car c WHERE c.cid = :cid")
    Offer findOfferOid(@Param(value = "cid") Long cid);

    @Query("SELECT c.utilities_utid FROM Car c WHERE c.cid = :cid")
    Utilities findUtilitiesUtid(@Param(value = "cid") Long cid);
}
