package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilitiesRepo extends JpaRepository<Utilities,Long> {
}
