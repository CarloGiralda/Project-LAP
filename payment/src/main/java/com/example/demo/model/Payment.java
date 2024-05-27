package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Getter
@Setter
public class Payment {

    private Long id;
    private String sender;
    private String receiver;
    private String price;
    private String signature;
    private boolean validated;


    public Payment(Long id,String sender, String receiver, String price, String signature){
        this.id = id;
        this.sender=sender;
        this.receiver=receiver;
        this.price=price;
        this.signature=signature;
        this.validated=false;
    }
}
