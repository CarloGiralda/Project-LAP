package com.example.CarSearch.repository;

import com.example.CarSearch.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;

public interface CarRepo extends JpaRepository<Car, Long> {
    @Query("SELECT c.brand, c.model from Car c WHERE c.cid = ?1")
    String findCarById(Long carId);

    @Query("SELECT c FROM Car c WHERE c.cid = ?1")
    Car findCarPreviewById(Long carId);

    @Query("SELECT c.image from Car c WHERE c.cid = ?1")
    byte[] findCarImageById(Long id);
}
