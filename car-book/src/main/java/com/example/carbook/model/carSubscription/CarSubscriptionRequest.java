package com.example.carbook.model.carSubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class CarSubscriptionRequest {
    private String userId;
    private ArrayList<Long> carsId;

}
