package com.example.CarInsertion.model;

import jakarta.persistence.*;
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
    private Long cid;
    private String startDate;
    private String peerId;

    public Auction(Long cid, String startDate, String peerId) {
        this.cid = cid;
        this.startDate = startDate;
        this.peerId = peerId;
    }
}
