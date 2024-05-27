package com.example.carbook.model.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimplifiedBooking {

    private Long cid;
    private String toDay;
    private String toHour;
}
