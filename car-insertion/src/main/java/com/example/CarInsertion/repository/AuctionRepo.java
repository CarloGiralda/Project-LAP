package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepo extends JpaRepository<Auction,Long> {
}
