package com.example.apigw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter()
@Setter
public class AuthenticationResponse {

    private final String token;
    private final String username;

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

}
