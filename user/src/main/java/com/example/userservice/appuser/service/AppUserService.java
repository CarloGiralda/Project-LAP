package com.example.userservice.appuser.service;

import com.example.userservice.appuser.discoveryclient.DiscoveryClientService;
import com.example.userservice.appuser.dto.PaymentRequestWallet;
import com.example.userservice.appuser.dto.SendEmailRequestDto;
import com.example.userservice.appuser.model.AppUser;
import com.example.userservice.appuser.model.UserSettings;
import com.example.userservice.appuser.repository.UserRepository;
import com.example.userservice.exception.*;
import com.example.userservice.registration.registrationtoken.ConfirmationToken;
import com.example.userservice.registration.registrationtoken.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {


    private final ConfirmationTokenService confirmationTokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final DiscoveryClientService discoveryClientService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("searching for user with email: {}", email);

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("user with email %s not found", email))
                );
    }

    public Long loadIdByUsername(String email) throws UsernameNotFoundException {
        log.info("searching for user with email: {}", email);

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("user with email %s not found", email))
                ).getId();
    }



    // activate user account once registration is validated
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public Long signUpUser(AppUser appUser) {
        boolean userExists = userRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User already exists");
        }

        // If user doesn't exist, encode the password
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        try {
            // Save user in the repository
            appUser = userRepository.save(appUser);
            log.info("User registered with ID: {}", appUser.getId());

            sendGenerateWalletRequest(appUser.getEmail());
            sendEmailConfirmation(appUser);

            return appUser.getId();

        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException("user already exists");
        }

        catch (Exception e) {

            log.error("Error during user registration", e);

            // Rollback user from the repository
            if (appUser.getId() != null) {
                userRepository.deleteById(appUser.getId());
                log.info("User rolled back from the repository");
            }

            throw new IllegalStateException("Error during user registration", e);
        }
    }

    public void sendEmailConfirmation(AppUser appUser){
        // Invoke the Eureka client to get the email service location
        String emailServiceUrl = discoveryClientService.getServiceUrl("SENDEMAIL-SERVICE") + "/email/sendEmail";
        log.info("Sending request to email service at {}", emailServiceUrl);

        // Create the send request
        SendEmailRequestDto sendRequest = createSendRequest(appUser);

        // Send the request to the email service
        ResponseEntity<String> response = restTemplate.postForEntity(emailServiceUrl, sendRequest, String.class);

        // Check the response status
        if (response.getStatusCode() != HttpStatus.OK) {

            log.error("Error from email service: {}", response.getBody());
            throw new SendEmailServiceException("Error sending confirmation email:" + response.getBody());
        }
    }

    public void sendGenerateWalletRequest(String username){
        // Invoke the Eureka client to get the email service location
        String paymentServiceUrl = discoveryClientService.getServiceUrl("PAYMENT-SERVICE") + "/payment/createUserWallet";
        log.info("Sending request to payment service at {}", paymentServiceUrl);

        PaymentRequestWallet paymentRequestWallet = new PaymentRequestWallet(username);

        // Send the request to the email service
        ResponseEntity<String> response = restTemplate.postForEntity(paymentServiceUrl, paymentRequestWallet, String.class);

        // Check the response status
        if (response.getStatusCode() != HttpStatus.OK) {

            log.error("Error from payment service: {}", response.getBody());
            throw new RuntimeException("Error during wallet creation:" + response.getBody());

        }
    }



    public String generateJwtToken(String username){
        //TODO check user account is enabled before generating the JWT token
        return jwtService.generateToken(username);
    }

    public String validateJwtToken(String token) {
        return jwtService.validateToken(token);
    }

    public String extractUsernameFromJwtToken(String token) {
        return jwtService.extractUsername(token);
    }

    public SendEmailRequestDto createSendRequest(AppUser appUser) {

        // Create random token
        String token = UUID.randomUUID().toString();

        // Create confirmation token with expiration date and related to the user
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        // Save on jpa repository
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return new SendEmailRequestDto(
                appUser.getEmail(),
                appUser.getFirstName(),
                token
                );
    }

    // confirm token for registration
    @Transactional
    public Boolean confirmRegistrationToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new ConfirmationTokenNotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenExpiredException("token expired");
        }

        int confirmedResponse = confirmationTokenService.setConfirmedAt(token);
        int enableResponse = this.enableAppUser(
                confirmationToken.getAppUser().getEmail());

        if(confirmedResponse + enableResponse == 2){
            log.info("user with id {} can now use its account ", confirmationToken.getId());
            return true;
        } else{
            return false;
        }
    }

    public UserSettings getUserSettings(String username) {
        String user = userRepository.findUserSettings(username);
        String[] data =  user.split(",");

        UserSettings userSettings = new UserSettings(
                data[0], // firstname
                data[1], // lastname
                data[2], // email
                data[3]  // contact
        );
        return userSettings;

    }
}
