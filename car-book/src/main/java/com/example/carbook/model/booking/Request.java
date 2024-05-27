package com.example.carbook.model.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {
    private String cid;
    private String madeDate;
    private String fromDay;
    private String toDay;
    private String fromHour;
    private String toHour;
    private String username;
    private String flag;
    private String description;

}
