package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "credentials")
public class Credentials {
    @Id
    private String username;
    @Column(length = 2047)
    private String publicKey;
    @Column(length = 2047)
    private String privateKey;

    public Credentials() {

    }
}
