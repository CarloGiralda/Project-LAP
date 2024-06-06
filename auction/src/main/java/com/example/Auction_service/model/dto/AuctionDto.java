package com.example.Auction_service.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuctionDto {

    private Long cid;
    private Long duration;
    private String peerId;
}