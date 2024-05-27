package com.notificationservice.notificationservice.dto;

import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarSubscriptionMessage {

    private Long carId;
    private String[] userIds;
}