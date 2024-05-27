package MACC_Project.Send_Email_MS.SendConfirmation.Pass;


import lombok.*;
import javax.persistence.*;


@Setter
@Getter
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Getter
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String residence;
    private String contact;

    @Enumerated(EnumType.STRING)
    private UserRole appUserRole;
    private Boolean locked = false;
    // TODO: Used to disable the user account until it validates the link sent by email
    private Boolean enabled = true;


}