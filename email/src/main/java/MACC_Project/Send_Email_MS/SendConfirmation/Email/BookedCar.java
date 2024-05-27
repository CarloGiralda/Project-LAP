package MACC_Project.Send_Email_MS.SendConfirmation.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedCar {

    private Long cid;
    private String renterUsername;
    private String searcherUsername;

}
