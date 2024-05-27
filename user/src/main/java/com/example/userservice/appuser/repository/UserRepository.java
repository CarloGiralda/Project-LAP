package com.example.userservice.appuser.repository;


import com.example.userservice.appuser.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE users u " +
            "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableAppUser(String email);

    @Query("SELECT u.firstName,u.lastName,u.email,u.contact FROM users u WHERE u.email = ?1")
    String findUserSettings(String email);

}
