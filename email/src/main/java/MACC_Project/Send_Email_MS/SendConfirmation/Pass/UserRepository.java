package MACC_Project.Send_Email_MS.SendConfirmation.Pass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User, Integer> {



    @Transactional
    @Modifying
    @Query(value = "UPDATE User u  " +
            "SET u.password = ?1 WHERE u.email = ?2")
    int updatePass(@Param("password") String password,
                   @Param("email") String  email);

    @Query(value = "SELECT * FROM USERS u",
            nativeQuery = true)
    User show();


    User findByEmail(String email);


}
