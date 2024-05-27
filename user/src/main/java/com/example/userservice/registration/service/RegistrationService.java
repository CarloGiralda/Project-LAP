package com.example.userservice.registration.service;

import com.example.userservice.appuser.model.AppUser;
import com.example.userservice.appuser.model.AppUserRole;
import com.example.userservice.appuser.service.AppUserService;
import com.example.userservice.exception.SendEmailServiceException;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserDataNotValidException;
import com.example.userservice.registration.dto.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final RegistrationFieldValidator fieldValidator;

    public Long register(RegistrationRequest request) {
        try {
            boolean isValidEmail = fieldValidator.testEmail(request.getEmail());
            boolean isValidFirstName = fieldValidator.testName(request.getFirstName());
            boolean isValidLastName = fieldValidator.testName(request.getLastName());

            if (isValidEmail && isValidFirstName && isValidLastName) {
                return appUserService.signUpUser(new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                ));
            }
            else {
                throw new UserDataNotValidException("user data are not valid");
            }

        } catch (UserDataNotValidException e) {
            throw new UserDataNotValidException("user data are not valid");
        }

        catch (SendEmailServiceException e) {
            // Handle specific exception for email sending
            log.error("Error during user registration", e);
            throw e; // Re-throw the exception
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        }
        catch (Exception e) {
            // Handle other exceptions
            log.error("Error during user registration", e);
            throw new RuntimeException("Failed to register the user account", e);
        }
    }

}
