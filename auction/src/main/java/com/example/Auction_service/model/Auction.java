package com.example.Auction_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @SequenceGenerator(name = "auctions_seq", sequenceName = "auctions_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auctions_seq")
    private Long auctionId;
    private Long cid;
    private Long duration;
    private String peerId;

    public Auction(Long cid, Long duration, String peerId) {
        this.cid = cid;
        this.duration = duration;
        this.peerId = peerId;
    }

}
