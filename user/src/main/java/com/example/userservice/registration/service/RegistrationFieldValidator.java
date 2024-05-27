package com.example.userservice.registration.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class RegistrationFieldValidator{

    // Regular expression for a simple email validation
    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    // TODO change it to include other names
    private static final String NAME_REGEX = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*(?: [A-Za-z]+(?:[ '-][A-Za-z]+)*)?$";


    private final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private final Pattern namePattern = Pattern.compile(NAME_REGEX);


    public boolean testEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public boolean testName(String firstname) {
        return namePattern.matcher(firstname).matches();
    }

}
