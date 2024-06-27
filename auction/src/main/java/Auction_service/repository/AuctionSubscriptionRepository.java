package Auction_service.repository;

import Auction_service.model.AuctionSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AuctionSubscriptionRepository extends JpaRepository<AuctionSubscription, Long> {

    @Query("SELECT a.peerId FROM AuctionSubscription a WHERE a.auction.auctionId = :auctionId")
    List<String> findPeerIdsByAuctionId(@Param("auctionId") Long auctionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AuctionSubscription a WHERE a.auction.auctionId = :auctionId")
    void deleteByAuctionId(@Param("auctionId") Long auctionId);

}
