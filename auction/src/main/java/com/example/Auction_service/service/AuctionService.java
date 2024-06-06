package com.example.Auction_service.service;


import com.example.Auction_service.model.Auction;
import com.example.Auction_service.model.AuctionSubscription;
import com.example.Auction_service.model.dto.SubscribeDto;
import com.example.Auction_service.repository.AuctionRepository;
import com.example.Auction_service.model.dto.AuctionDto;
import com.example.Auction_service.repository.AuctionSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

            auctionRepository.save(new Auction(request.getCid(),request.getDuration(),request.getPeerId()));

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }
    }

    public void subscribeToAuction(SubscribeDto request) throws Exception {
        try {

            Auction auction = auctionRepository.findAuctionByCid(request.getCid());

            AuctionSubscription auctionSubscription = new AuctionSubscription(request.getPeerId(), auction);

            auctionSubscriptionRepository.save(auctionSubscription);

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }
    }


    public List<String> getPeers(Long auctionId) throws Exception {
        try {


           return auctionSubscriptionRepository.findPeerIdsByAuctionId(auctionId);

        } catch (DataIntegrityViolationException e){
            log.info("constraints exception");
            throw new Exception(e.getMessage());

        }
    }


}

