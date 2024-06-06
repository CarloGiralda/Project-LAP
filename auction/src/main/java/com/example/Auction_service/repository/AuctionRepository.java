package com.example.Auction_service.repository;

import com.example.Auction_service.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Auction findAuctionByCid(Long cid);
}
