package com.example.carbook.controller;

import com.example.carbook.model.booking.*;
import com.example.carbook.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/reservation")
@RestController
public class BookController {

    private final BookingService bookingService;

    @PostMapping(path = "/book")
    @ResponseBody
    public ResponseEntity<String> bookCar(@RequestBody Request request, @RequestHeader("Logged-In-User") String username){
        try {
            Long cid=Long.parseLong(request.getCid());
            Booking booking = new Booking(
                    cid,
                    request.getFromDay(),
                    request.getToDay(),
                    request.getFromHour(),
                    request.getToHour(),
                    request.getMadeDate(),
                    request.getUsername(),
                    request.getDescription()
                    );

            bookingService.setBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body("Request completed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // TODO not anymore required
    @PostMapping(path = "/listBookings")
    @ResponseBody
    public ResponseEntity<?> listBookedCar(@RequestBody Request request, @RequestHeader("Logged-In-User") String username) {
        try {
            String param;
            switch (request.getFlag()){
                case "username" -> param= "";
                case "username&from" -> param= request.getFromDay();
                case "username&madeDate" -> param= request.getMadeDate();
                case "username&to" -> param= request.getToDay();
                default -> param= request.getCid();
            }
            ArrayList<Booking> ret= bookingService.getBooking(request.getUsername(),request.getFlag(),param);
            if(!ret.isEmpty()) {
                return new ResponseEntity<>(ret, HttpStatus.OK);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping(path = "/extendBooking")
    public ResponseEntity<String> extendBook(@RequestBody ExtendBookingRequest request, @RequestHeader("Logged-In-User") String username){
        try {

            bookingService.extendBooking(request);

            return ResponseEntity.status(HttpStatus.OK).body("Request completed");
        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(path = "/bookForAuction")
    public ResponseEntity<Long> bookForAuction(@RequestBody AuctionRequest request, @RequestHeader("Logged-In-User") String username){
        try {
            Long bid = bookingService.setBookingForAuction(request.getCid(), username);
            if (bid != null) {
                return ResponseEntity.status(HttpStatus.OK).body(bid);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }



    @GetMapping(path = "/listBookingsPreview")
    public ResponseEntity<?> listBookingsPreview(@RequestHeader("Logged-In-User") String username) {
        try {
            log.info(username);
            ArrayList<BookingPreview> ret= bookingService.getBookingPreview(username);
            if(!ret.isEmpty()) {
                return new ResponseEntity<>(ret, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no reservation");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping(path = "/listBookingsHistory")
    public ResponseEntity<?> listBookingsHistory(@RequestHeader("Logged-In-User") String username) {
        try {

            ArrayList<BookingPreview> ret= bookingService.getBookingHistory(username);
            if(!ret.isEmpty()) {
                return new ResponseEntity<>(ret, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no reservation");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping(path = "/getCarAvailability/{carId}")
    public ResponseEntity<?> getAvailability(@PathVariable Long carId) {
        try {
            log.info("request for car availability");
            Boolean response = bookingService.getCarAvailability(carId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @DeleteMapping(path = "/deleteBooking/{bid}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bid) {
        bookingService.deleteBooking(bid);
        return ResponseEntity.status(HttpStatus.OK).body("Booking deleted");
    }
}
