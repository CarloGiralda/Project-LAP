package com.example.userservice.appuser.controller;

import com.example.userservice.appuser.dto.LoginRequest;
import com.example.userservice.appuser.model.AppUser;
import com.example.userservice.appuser.model.UserSettings;
import com.example.userservice.appuser.service.AppUserService;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserDataNotValidException;
import com.example.userservice.registration.dto.RegistrationRequest;
import com.example.userservice.registration.service.RegistrationService;

import com.example.userservice.security.PasswordEncoder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/auth")
public class UserAuthController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            Long id = registrationService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User saved with id " + id + " check your email to confirm the account");

        } catch (UserDataNotValidException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user data not valid");
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user already exists");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // on login the user generate a new jwt token given its email
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJwtToken(@RequestBody LoginRequest request){


        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        log.info("login request: {}",request);

        if(authenticate.isAuthenticated()){
            String jwtToken = appUserService.generateJwtToken(request.getEmail());

            // map as json object the token
            Map<String,String> responseBody = new HashMap<>();
            responseBody.put("jwt", jwtToken);

            // return the jwt token if ok
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED) ;
        }else{
            return new ResponseEntity<>("invalid access",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/validate")
    public ResponseEntity<?> validateJwtToken(@RequestParam("token") String token){
        try {
            String validatedToken = appUserService.validateJwtToken(token);
            if(validatedToken != null){
                log.info("jwt token validated");
                return new ResponseEntity<>("validated",HttpStatus.OK);
            } else {
                return new ResponseEntity<>("not valid",HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            return new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/extractUsernameFromJwt")
    public ResponseEntity<?> extractUsernameFromJwt(@RequestParam("token") String token){
        try {
            String usernameFromJwtToken = appUserService.extractUsernameFromJwtToken(token);
            log.info("username extracted: {}", usernameFromJwtToken);
            Map<String,String> responseBody = new HashMap<>();
            responseBody.put("username", usernameFromJwtToken);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("cannot extract username from jwt",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getUserFromJwt")
    public ResponseEntity<?> extractUserFromJwt(@RequestParam("jwt") String jwt) {
        try {
            String validatedToken = appUserService.validateJwtToken(jwt);
            if (validatedToken != null) {
                String email = appUserService.extractUsernameFromJwtToken(jwt);
                UserDetails user = appUserService.loadUserByUsername(email);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getUserIdFromUsername")
    public ResponseEntity<?> extractId(@RequestParam("username") String username) {
        try {
            Long userId = appUserService.loadIdByUsername(username);
            Map<String,String> responseBody = new HashMap<>();
            responseBody.put("username", userId.toString());
            log.info("sending id {} for user {}", userId, username);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestHeader("username") String username) {
        try {
            UserSettings userSettings = appUserService.getUserSettings(username);
            log.info("user settings sent");
            return new ResponseEntity<>(userSettings, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
