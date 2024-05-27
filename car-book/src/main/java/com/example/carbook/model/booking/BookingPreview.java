package com.example.carbook.model.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPreview {

    private Long bid;
    private Long cid;
    private String fromDay;
    private String toDay;
    private String toHour;
    private String madeDate;

}
