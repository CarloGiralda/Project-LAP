package MACC_Project.Send_Email_MS.SendConfirmation.Email;


import MACC_Project.Send_Email_MS.SendConfirmation.Pass.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(path="/email")
@AllArgsConstructor
public class SendEmailController {

    private  SendEmailService emailSender;
    private  UserService user;


    /**
    * Send confirmation email for user registration
     * **/
    @PostMapping(path = "/sendEmail")
    public ResponseEntity<?> sendConfirmationEmail(@RequestBody SendRequest request) {
        try {
            emailSender.send(request.getEmail(), request.getFirstName(), request.getConfirmationToken());
            return ResponseEntity.status(HttpStatus.OK).body("Done");
        } catch (Exception e){
            log.error("error sending confirmation email ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email for account confirmation");
        }
    }

    /**
     * Send payment email when a new booking is added to the blockchain
     * **/
    @PostMapping(path = "/sendPaymentEmail")
    public ResponseEntity<?> sendExecutedPaymentEmail(@RequestBody ExecutedPayment executedPayment) {
        try {
            String result =  emailSender.send(executedPayment.getSenderUsername(),executedPayment.getReceiverUsername() + "-" + executedPayment.getPrice().toString(), "Payment");
            return ResponseEntity.ok(result);
        } catch (Exception e){
            log.error("error sending confirmation email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send payment execution message");
        }
    }


    /**
     * Send payment email when a new booking is stored in the booking service
     * **/
    @PostMapping(path = "/sendCarBookedEmail")
    public ResponseEntity<?> sendCarBookedEmail(@RequestBody BookedCar bookedCar) {
        try {

            String result =  emailSender.send(bookedCar.getRenterUsername(), bookedCar.getCid() +"-"+bookedCar.getSearcherUsername() , "Booking");
            return ResponseEntity.ok(result);
        } catch (Exception e){
            log.error("error sending confirmation email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send booking executed message");
        }
    }

    @PostMapping(path = "/sendZoneSubscriptionEmail")
    public ResponseEntity<?> sendZoneSubscription(@RequestBody ZoneSubscription zoneSub) {
        try {

            String result =  emailSender.send(zoneSub.getTo(), zoneSub.getCid() , "Zone");
            return ResponseEntity.ok(result);
        } catch (Exception e){
            log.error("error sending confirmation email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send booking executed message");
        }
    }





    @GetMapping(path = "/recovery")
    public ResponseEntity<String> sendRecoveryEmail(@RequestParam("email") String email) {
        // Generate random password
        String pass=UUID.randomUUID().toString();
        log.info("Sending Recovery Email...");

        // update password with a new random value
        if(user.updatePass(email, pass)!=1){
            log.error("Recovery Procedure Failed due to internal error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to recover password");
        }

        // send back by email
        if(emailSender.send(email, pass, "Recovery") == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send recovery password email");

        log.info("Done, Recovery email has been sent to your registered email address");
        return ResponseEntity.ok("Recovery password email has been sent correctly");
    }


    @PostMapping(path = "/userSettings/modify")
    public ResponseEntity<String> modifyUserSettings(@RequestBody SendRequest request, @RequestHeader("Logged-In-User") String username) {
        try {
            log.info("updating user settings for user {}", username);
            if (user.updateNonNullFields(request) != 1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to modify user settings");
            } else {
                return ResponseEntity.ok("User settings modified successfully");
            }
        } catch (Exception e){
            log.error("Error modifying user settings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

    }





}

