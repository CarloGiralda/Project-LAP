package MACC_Project.Send_Email_MS.SendConfirmation.Email;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SendRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String confirmationToken;
}
