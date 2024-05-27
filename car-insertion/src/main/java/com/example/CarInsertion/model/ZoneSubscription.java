package com.example.CarInsertion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZoneSubscription {

    private String to;
    private String cid;
    // TODO also need zone location name
}