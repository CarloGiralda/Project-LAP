package MACC_Project.Send_Email_MS.SendConfirmation.Pass;

import MACC_Project.Send_Email_MS.SendConfirmation.Email.SendRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public User all () {
        return userRepository.show();

    }

    public int updateNonNullFields(SendRequest user){
        if(user.getEmail()!=null) {
            User user1 = userRepository.findByEmail(user.getEmail());
            if(!Objects.isNull(user.getFirstName()) && !Objects.equals(user.getFirstName(),"")){
                user1.setFirstName(user.getFirstName());
            }
            if(!Objects.isNull(user.getLastName()) && !Objects.equals(user.getLastName(),"")){

                user1.setLastName(user.getLastName());
            }
            if(!Objects.isNull(user.getPassword()) && !Objects.equals(user.getPassword(),"")){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user1.setPassword(encodedPassword);
            }
            /*if(!Objects.isNull(user.getContact()) && !Objects.equals(user.getContact(),""))
                user1.setContact(user.getContact());
            if(!Objects.isNull(user.getResidence()) && !Objects.equals(user.getResidence(),""))
                user1.setResidence(user.getResidence());*/
            userRepository.save(user1);
            return 1;
        }
        return 0;
    }
    public int updatePass(String email, String  pass) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(pass);
        if(userRepository.updatePass(encodedPassword, email)>0)
            return 1;
        else
            return 0;
    }

}