package Auction_service.controller;


import Auction_service.model.dto.AuctionDto;
import Auction_service.model.dto.SubscribeDto;
import Auction_service.service.AuctionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/auction")
@RestController
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping(path = "/subscribe")
    @ResponseBody
    public ResponseEntity<String> subscribe(@RequestBody SubscribeDto request, @RequestHeader("Logged-In-User") String username){
        try {
            String ts = auctionService.subscribeToAuction(request);
            System.out.println(ts);
            return ResponseEntity.status(HttpStatus.CREATED).body("\"" + ts + "\""); // Wrap ts in double quotes
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<String> createAuction(@RequestBody AuctionDto request){
        try {

            auctionService.saveAuction(request);

            return ResponseEntity.status(HttpStatus.CREATED).body("2");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/get_peer_list")
    public ResponseEntity<?> readPeerList(@RequestParam("auctionId") Long auctionId, @RequestHeader("Logged-In-User") String username){
        try {

            List<String> peers =  auctionService.getPeers(auctionId);

            if (peers.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("List is empty");
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body(peers);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> getAllAuction(@RequestHeader("Logged-In-User") String username){
        try {

            return ResponseEntity.status(HttpStatus.OK).body(auctionService.getAllAuction());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/delete_auction")
    public ResponseEntity<?> deleteAuction(@RequestParam("auctionId") Long auctionId,@RequestHeader("Logged-In-User") String username){
        try {
            auctionService.deleteAuction(auctionId);
            return ResponseEntity.status(HttpStatus.OK).body("auction removed");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
