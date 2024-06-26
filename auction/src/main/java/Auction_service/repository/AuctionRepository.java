package Auction_service.repository;

import Auction_service.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Auction findAuctionByAuctionId(Long auctionId);
}
