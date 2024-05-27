package MACC_Project.Send_Email_MS.SendConfirmation.Email;

import MACC_Project.Send_Email_MS.SendConfirmation.discoveryclient.DiscoveryClientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {


    private final JavaMailSender mailSender;
    private final DiscoveryClientService discoveryClientService;

    @Value("${app.email-from}")
    private String emailFrom; // get email-from url from file

    @Value("${app.frontend-url}")
    private String frontEndUrl; // get email-from url from file


    private static final String confirmationEndpoint = "/confirmation/confirm?token=";

    @Async
    public String send(String to, String content, String flag) {
        String email;
        String subject;
        String link;

        switch (flag) {
            case "Zone":
                email = buildEmail(content, "Zone");
                subject = "New car in your zone";
                break;
            case "Payment":
                email = buildEmail(content, "Payment");
                subject = "Booking - Transaction executed";
                break;
            case "Recovery":
                link = frontEndUrl;
                email = buildRecoveryEmail(content, link);
                subject = "Password Recovery Procedure";
                break;
            case "Booking":
                email = buildEmail(content, "Booking");
                subject = "Booking - Car rent";
                break;
            default:
                // CONTACT USER SERVICE
                link = discoveryClientService.getServiceUrl("USER-SERVICE") + confirmationEndpoint;
                log.info("sending confirmation token request to " + link);
                email = buildConfirmationEmail(content, link + flag);
                subject = "Email Confirmation Procedure";
                break;
        }

        try {
            sendEmail(to, email, subject);
            log.info("email sent to {}", to);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("Failed to send email ", e);
        }

        return "Done";
    }

    private void sendEmail(String to, String emailContent, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(emailContent, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(emailFrom); // TODO change with website email
        mailSender.send(mimeMessage);
    }

    private String buildRecoveryEmail(String content, String link) {
        return buildEmail(content, link);
    }

    private String buildConfirmationEmail(String content, String link) {
        return buildEmail(content, link);
    }

    public String buildEmail(String content, String link) {

        String emailMessage;
        String emailTitle;

        if (Objects.equals(link,"Payment")){ // executed payment case
            emailMessage = buildEmailMessage(EmailType.PAYMENT, content, link);
            emailTitle = "Payment executed";
        }
        else if (Objects.equals(link,"Booking")) {
            emailMessage = buildEmailMessage(EmailType.BOOKING, content, link);
            emailTitle = "Booking executed";
        }
        else if (Objects.equals(link,"Zone")) {
            emailMessage = buildEmailMessage(EmailType.ZONE, content, link);
            emailTitle = "Zone notification";
        } else if(Objects.equals(link, frontEndUrl)){ // password recovery case
            emailMessage = buildEmailMessage(EmailType.RECOVERY, content, link);
            emailTitle = "Recover your password";

        } else { // confirmation account case
            emailMessage = buildEmailMessage(EmailType.CONFIRMATION, content, link);
            emailTitle = "Confirm your email";
        }
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">"+emailTitle+"</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" + emailMessage +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildEmailMessage(EmailType emailType, String content, String link) {
        String message;
        String greeting = "This is a message automatically generated from our website. Please don't reply to it.\n";
        String signature = "<p>See you soon</p>";

        switch (emailType) {
            case PAYMENT:
                String[] paymentDetails = content.split("-");
                String receiver = paymentDetails[0];
                String amount = paymentDetails[1];
                message = String.format(
                        "%sYour payment of %s coins has been successfully made to the renter %s\n",
                        greeting, amount, receiver
                );
                break;

            case BOOKING:
                String[] bookingDetails = content.split("-");
                String cid = bookingDetails[0];
                String searcherUsername = bookingDetails[1];
                message = String.format(
                        "%sYour car with id %s has been rented by %s. Now you can agree on the final details using our chat\n",
                        greeting, cid, searcherUsername
                );
                break;

            case ZONE:
                link = "http://localhost:8081/carsearch/" + content;
                message = String.format(
                        "A new car, with id %s has been added to an area you were interested in. You can check it here %s\n",
                        content, link
                );

                break;
            case RECOVERY:
                message = String.format(
                        "%sThis is your new password:\n%s\n",
                        greeting, content
                );
                link += "/login"; // Adjust link for recovery case
                break;

            default: // ACTIVATION
                message = String.format(
                        "%sHi %s,\n%s Click the link to activate account.\n",
                        greeting, content, link
                );
                break;
        }
        log.info(link);
        return String.format(
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">%s</p>"
                        + "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">%s</p>"
                        + "<blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">"
                        + "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"%s\">%s</a></p>"
                        + "</blockquote>\n%s",
                message, "", link, "", signature
        );

    }


}
