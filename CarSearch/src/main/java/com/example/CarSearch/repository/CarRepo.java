package com.example.CarSearch.repository;

import com.example.CarSearch.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface CarRepo extends JpaRepository<Car,Long>, JpaSpecificationExecutor<Car> {
}
