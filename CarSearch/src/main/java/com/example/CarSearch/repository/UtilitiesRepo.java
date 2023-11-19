package com.example.CarSearch.repository;

import com.example.CarSearch.model.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilitiesRepo extends JpaRepository<Utilities,Long>, JpaSpecificationExecutor<Utilities> {
}
