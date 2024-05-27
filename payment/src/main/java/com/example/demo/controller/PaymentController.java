package com.example.demo.controller;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.dto.PaymentRequestWallet;
import com.example.demo.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path="/payment")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping(path = "/createTransaction")
    public ResponseEntity<?> insertTransaction(@RequestBody PaymentDTO paymentDTO){
        try {
            paymentService.sendTransaction(paymentDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Done");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping(path = "/getBalance")
    public ResponseEntity<?> getBalance(@RequestHeader("Logged-In-User") String username){
        try {

            Long balance = paymentService.getUserBalance(username);
            return ResponseEntity.status(HttpStatus.OK).body(balance);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @PostMapping(path = "/createUserWallet")
    public ResponseEntity<?> insertTransaction(@RequestBody PaymentRequestWallet requestWallet){
        try {
            paymentService.generateWallet(requestWallet.getUsername());

            // this is done just for the sake of presentation
            // each new user is gifted with 1000 coins by the protocol, that he can spend
            PaymentDTO paymentDTO = new PaymentDTO("GIFTER", requestWallet.getUsername(), 1000L);
            paymentService.sendTransaction(paymentDTO);

            return ResponseEntity.status(HttpStatus.OK).body("created");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
