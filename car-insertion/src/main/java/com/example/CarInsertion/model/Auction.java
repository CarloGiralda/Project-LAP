package com.example.CarInsertion.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Auction {

    private Long cid;
    private String startDate;
    private String peerId;

    public Auction(Long cid, String startDate, String peerId) {
        this.cid = cid;
        this.startDate = startDate;
        this.peerId = peerId;
    }
}
