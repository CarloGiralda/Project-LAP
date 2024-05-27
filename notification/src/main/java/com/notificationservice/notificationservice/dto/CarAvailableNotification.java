package com.notificationservice.notificationservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarAvailableNotification {

    private Long carId;
    private String carName;
    private String carUrl;
}
