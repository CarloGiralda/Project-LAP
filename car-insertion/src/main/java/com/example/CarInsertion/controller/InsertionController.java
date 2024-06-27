package com.example.CarInsertion.controller;

import com.example.CarInsertion.model.*;
import com.example.CarInsertion.service.InsertionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping(path = "/carinsert")
@RestController
public class InsertionController {
    private InsertionService insertionService;
    private InsertionDTO dto;
    private AuctionDTO auctionDTO;
    private boolean bool;

    public InsertionController(InsertionService insertionService) {
        super();
        this.bool = false;
        this.dto = new InsertionDTO();
        this.auctionDTO = new AuctionDTO();
        this.insertionService = insertionService;
    }

    @PostMapping(path = "/insertoffer")
    public ResponseEntity<String> saveOfferForm(@RequestBody Offer offer, @RequestHeader("Logged-In-User") String username) {
        if (offer.hasEmptyFields()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        if (this.bool){
            auctionDTO.setOffer(offer);
        } else {
            this.dto.setOffer(offer);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/insertauction")
    public ResponseEntity<String> saveAuctionForm(@RequestBody Auction auction, @RequestHeader("Logged-In-User") String username) {
        this.auctionDTO.setAuction(auction);
        this.bool = true;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/insertcar", consumes = {"multipart/form-data"})
    public ResponseEntity<String> saveCarForm(@ModelAttribute CarDTO cardto, @RequestHeader("Logged-In-User") String username) throws IOException {
        Car car = new Car(cardto.getPlateNum(), Long.parseLong(cardto.getYear()), cardto.getPollutionLevel(), cardto.getFuel(), cardto.getBrand(),
                Long.parseLong(cardto.getPassengers()), cardto.getModel(), cardto.getClassification(), Long.parseLong(cardto.getCarDoorNumber()), cardto.getEngine(),
                cardto.getTransmission(), cardto.getImage().getBytes());
        if (car.hasEmptyFields()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
        if (this.bool) {
            this.auctionDTO.setCar(car);
        } else {
            this.dto.setCar(car);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/insertutilities")
    public ResponseEntity<String> saveUtilitiesForm(@RequestBody Utilities ut, @RequestHeader("Logged-In-User") String username) {
        if (ut.hasEmptyFields()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
        if (this.bool) {
            this.auctionDTO.setUtilities(ut);
            insertionService.insertAuction(this.auctionDTO);
        } else {
            this.dto.setUtilities(ut);
            insertionService.insert(this.dto);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/listcars")
    public ResponseEntity<?> retrieveCarsByUsername(@RequestHeader("Logged-In-User") String username) {
        List<InsertionDTO> cars = insertionService.retrieveCars(username);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }


    @PostMapping(path = "/modify/{id}")
    public ResponseEntity<String> saveModification(@PathVariable Long id, @RequestBody InsertionDTO dto, @RequestHeader("Logged-In-User") String username) {
        dto.getCar().setCid(id);
        insertionService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
