package com.example.CarInsertion.service;

import com.example.CarInsertion.discoveryclient.DiscoveryClientService;
import com.example.CarInsertion.model.*;
import com.example.CarInsertion.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class InsertionServiceImpl implements InsertionService{
    private final CarRepo carRepo;
    private final OfferRepo offerRepo;
    private final AuctionRepo auctionRepo;
    private final UtilitiesRepo utilitiesRepo;
    private final CarOfferUtRepo carOfferUtRepo;

    private DiscoveryClientService discoveryClientService;

    private RestTemplate restTemplate;

    public InsertionServiceImpl(CarRepo carRepo, OfferRepo offerRepo, AuctionRepo auctionRepo, UtilitiesRepo utilitiesRepo, CarOfferUtRepo carOfferUtRepo, DiscoveryClientService discoveryClientService, RestTemplate restTemplate) {
        this.carRepo = carRepo;
        this.offerRepo = offerRepo;
        this.auctionRepo = auctionRepo;
        this.utilitiesRepo = utilitiesRepo;
        this.carOfferUtRepo = carOfferUtRepo;
        this.discoveryClientService = discoveryClientService;
        this.restTemplate = restTemplate;
    }

    @Override
    public InsertionDTO insert(InsertionDTO dto) {
        Offer offer = dto.getOffer();
        Utilities ut = dto.getUtilities();
        Car car = dto.getCar();
        Offer resultOffer = offerRepo.save(offer);
        Utilities resultUt = utilitiesRepo.save(ut);
        car.setOffer_oid(resultOffer);
        car.setUtilities_utid(resultUt);
        Car resultCar = carRepo.save(dto.getCar());

        // TODO
        notifySubscribedUser(car.getCid(),offer.getZoneLocation());

        return new InsertionDTO(resultCar, resultOffer, resultUt);
    }

    @Override
    public AuctionDTO insertAuction(AuctionDTO dto) {
        Auction auction = dto.getAuction();
        Utilities ut = dto.getUtilities();
        Car car = dto.getCar();
        auction.setCid(car.getCid());
        Auction resultAuction = auctionRepo.save(auction);
        Utilities resultUt = utilitiesRepo.save(ut);
        car.setUtilities_utid(resultUt);
        Car resultCar = carRepo.save(dto.getCar());

        String auctionUrl = discoveryClientService.getServiceUrl("AUCTION-SERVICE")+  "/auction/create";
        restTemplate.postForEntity(auctionUrl, auction, String.class);

        return new AuctionDTO(resultCar, resultAuction, resultUt);
    }

    private void notifySubscribedUser(Long cid, String zone){
        // 1) GET USER SUBSCRIBED FOR THE ZONE OF THE CAR
        List<String> subUsers = getSubscribedUser(zone);

        // 2) SEND EMAIL TO SUBSCRIBED USER
        if (!subUsers.isEmpty()){

            for(String user: subUsers){
                sendEmail(user, cid);
            }
        }
    }

    private List<String> getSubscribedUser(String zone) {
        String[] coordinates = zone.split("/");
        String lat = coordinates[0];
        String lon = coordinates[1];
        // send the request to area service
        String zoneServiceUrl = discoveryClientService.getServiceUrl("AREA-SERVICE") + "/area/getSubscribedUser?lat=" + lat + "&lon=" + lon;
        log.info("Sending request to area service at {}", zoneServiceUrl);

        // make the request to area service
        ResponseEntity<List> response = restTemplate.getForEntity(zoneServiceUrl, List.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("error in the area service");
        } else {
            return response.getBody();
        }
    }


    private void sendEmail(String to, Long cid){
        // send request to email server
        String emailServiceUrl = discoveryClientService.getServiceUrl("SENDEMAIL-SERVICE") + "/email/sendZoneSubscriptionEmail";
        log.info("Sending request to email service at {}", emailServiceUrl);

        ZoneSubscription zoneSubscription = new ZoneSubscription(to, Long.toString(cid));

        // make the request to email service
        ResponseEntity<String> response = restTemplate.postForEntity(emailServiceUrl, zoneSubscription, String.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("error in the email sender");
        }
    }


    @Override
    @Transactional
    public int update(InsertionDTO dto) {
        Offer offer = dto.getOffer();
        Utilities ut = dto.getUtilities();
        Car car = dto.getCar();
        Long offerid = carRepo.findOfferOid(car.getCid()).getOid();
        Long utid = carRepo.findUtilitiesUtid(car.getCid()).getUtid();
        offer.setOid(offerid);
        ut.setUtid(utid);
        int carRes = carOfferUtRepo.updateCar(car);
        int offerRes = carOfferUtRepo.updateOffer(offer);
        int utRes = carOfferUtRepo.updateUtilities(ut);

        if (carRes == 1 && offerRes == 1 && utRes == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<InsertionDTO> retrieveCars(String username) {
        return carOfferUtRepo.getCarOfferUtFromUsername(username);
    }
}
