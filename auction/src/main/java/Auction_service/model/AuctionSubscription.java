package Auction_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "auctions_sub")
public class AuctionSubscription {

    @Id
    @SequenceGenerator(name = "auctions_sub_seq", sequenceName = "auctions_sub_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auctions_sub_seq")
    private Long subId;
    private String peerId;

    @ManyToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "auctionId")
    private Auction auction;

    public AuctionSubscription(String peerId, Auction auction) {
        this.peerId = peerId;
        this.auction = auction;
    }



}