package Auction_service.service;



import Auction_service.model.Auction;
import Auction_service.model.AuctionSubscription;
import Auction_service.model.dto.AuctionDto;
import Auction_service.model.dto.SubscribeDto;
import Auction_service.repository.AuctionRepository;
import Auction_service.repository.AuctionSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionSubscriptionRepository auctionSubscriptionRepository;


    public AuctionService(AuctionRepository auctionRepository, AuctionSubscriptionRepository auctionSubscriptionRepository) {
        this.auctionRepository = auctionRepository;
        this.auctionSubscriptionRepository = auctionSubscriptionRepository;
    }

    public void saveAuction(AuctionDto request) throws Exception {
        try {

            auctionRepository.save(new Auction(request.getCid(),request.getStartDate(),request.getPeerId()));

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }
    }

    public String subscribeToAuction(SubscribeDto request) throws Exception {
        String date = null;
        try {

            Auction auction = auctionRepository.findAuctionByAuctionId(request.getAuctionId());

            date = auction.getStartDate();

            AuctionSubscription auctionSubscription = new AuctionSubscription(request.getPeerId(), auction);

            auctionSubscriptionRepository.save(auctionSubscription);

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }
        return date;
    }

    public List<String> getPeers(Long auctionId) throws Exception {
        try {
           return auctionSubscriptionRepository.findPeerIdsByAuctionId(auctionId);
        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());
        }
    }

    public List<Auction> getAllValidAuctions() throws Exception {
        List<Auction> auctions = auctionRepository.findAll();
        List<Auction> validAuctions = new ArrayList<>();
        for (Auction auction : auctions) {
            if (isAfterCurrentTime(auction.getStartDate())) {
                validAuctions.add(auction);
            }
        }
        return validAuctions;
    }

    public void deleteAuction(Long auctionId) throws Exception{
        try {

            // first remove all auction subscription
            auctionSubscriptionRepository.deleteByAuctionId(auctionId);

            // then remove
            auctionRepository.deleteById(auctionId);
        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());
        }
    }

    private static boolean isAfterCurrentTime(String isoDateTime) {
        OffsetDateTime dateTime = OffsetDateTime.parse(isoDateTime);
        OffsetDateTime currentTime = OffsetDateTime.now().plusHours(2);
        return dateTime.isAfter(currentTime);
    }
}

