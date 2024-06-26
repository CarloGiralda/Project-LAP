package Auction_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SubscribeDto {
    private Long auctionId;
    private String peerId;
}
