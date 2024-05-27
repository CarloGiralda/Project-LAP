package com.notificationservice.notificationservice.JsonModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

    @JsonProperty("username")
    private String username;

}
