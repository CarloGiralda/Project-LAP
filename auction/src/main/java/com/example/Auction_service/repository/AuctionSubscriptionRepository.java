package com.example.Auction_service.repository;

import com.example.Auction_service.model.AuctionSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AuctionSubscriptionRepository extends JpaRepository<AuctionSubscription, Long> {

    @Query("SELECT a.peerId FROM AuctionSubscription a WHERE a.auction.auctionId = :auctionId")
    List<String> findPeerIdsByAuctionId(@Param("auctionId") Long auctionId);

}
