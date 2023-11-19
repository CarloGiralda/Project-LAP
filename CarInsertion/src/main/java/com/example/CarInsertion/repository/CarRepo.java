package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<Car,Long> {
}
