package com.example.carbook.model.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedCar {

    private Long cid;
    private String renterUsername;
    private String searcherUsername;

}
