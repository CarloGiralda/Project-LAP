package com.example.demo.repository;

import com.example.demo.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, String> {
    Credentials findByUsername(String username);

    Credentials findCredentialsByPublicKey(String username);
}
