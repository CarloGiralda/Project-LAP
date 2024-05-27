package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UtilitiesRepo extends JpaRepository<Utilities,Long> {
}
