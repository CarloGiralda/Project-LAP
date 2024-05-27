package com.example.userservice.registration.registrationtoken;

import com.example.userservice.appuser.service.AppUserService;
import com.example.userservice.exception.EmailAlreadyConfirmedException;
import com.example.userservice.exception.ConfirmationTokenExpiredException;
import com.example.userservice.exception.ConfirmationTokenNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/confirmation")
@AllArgsConstructor
public class ConfirmationTokenController {


    private AppUserService appUserService;

    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        try {
            if (appUserService.confirmRegistrationToken(token)) {
                return new ResponseEntity<>("Registration token validated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Registration token not valid", HttpStatus.BAD_REQUEST);
            }
        } catch (ConfirmationTokenNotFoundException e) {
            return new ResponseEntity<>("Token not found", HttpStatus.NOT_FOUND);
        } catch (EmailAlreadyConfirmedException e) {
            return new ResponseEntity<>("Email already confirmed", HttpStatus.BAD_REQUEST);
        } catch (ConfirmationTokenExpiredException e) {
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the exception (optional)
            log.error("Error confirming registration token", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
