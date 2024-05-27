package com.example.zone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private String username;
    private String locationName;
    private double center_latitude;
    private double center_longitude;
    private double radius;
}
