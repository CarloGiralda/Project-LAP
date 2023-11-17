package com.example.CarSearch.repository;

import com.example.CarSearch.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepo extends JpaRepository<Offer,Long>, JpaSpecificationExecutor<Offer> {
}
