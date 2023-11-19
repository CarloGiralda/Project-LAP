package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepo extends JpaRepository<Offer,Long> {
}
