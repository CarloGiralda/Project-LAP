package com.example.carbook.model.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtendBookingRequest {
    private Long bid;
    private Long cid;
    private String toDay;
    private String toHour;


}
