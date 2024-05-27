package MACC_Project.Send_Email_MS.SendConfirmation.Email;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ExecutedPayment {

    private String senderUsername;
    private String receiverUsername;
    private Long price;

}
