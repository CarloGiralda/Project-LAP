package com.example.userservice.appuser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSettings {

    private String firstName;
    private String lastName;
    private String email;
    private String contact;
}
