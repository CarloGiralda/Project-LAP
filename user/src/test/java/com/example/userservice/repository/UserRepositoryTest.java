package com.example.userservice.repository;


import com.example.userservice.appuser.model.AppUser;
import com.example.userservice.appuser.model.AppUserRole;
import com.example.userservice.appuser.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void givenNewUser_whenSave_returnId(){
        AppUser testUser = new AppUser("test-admin","test-admin","test-admin@gmail.com","password", AppUserRole.USER);

        // save is a method provided by jpa infrastructure
        Long id = userRepository.save(testUser).getId();
        Assertions.assertThat(id).isGreaterThan(1);

    }

    @Test
    @Rollback
    public void givenEmail_whenUpdate_returnId(){
        AppUser testUser = new AppUser("test-admin","test-admin","test-admin@gmail.com","password", AppUserRole.USER);


        // save the user
        userRepository.save(testUser);
        int affectedRows = userRepository.enableAppUser(testUser.getEmail());
        Assertions.assertThat(affectedRows).isGreaterThan(0);

    }


}
