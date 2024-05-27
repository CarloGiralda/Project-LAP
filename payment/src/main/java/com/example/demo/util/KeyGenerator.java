package com.example.demo.util;



import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class KeyGenerator {


    public String[] generateKeyPair(String username) throws Exception {

        // generate key pair
        KeyPair keyPair = generateKeyPair();
        String privateKeyString = convertPrivateKeyToPEM(keyPair.getPrivate());
        String publicKeyString = convertPublicKeyToPEM(keyPair.getPublic());

        return new String[]{publicKeyString, privateKeyString};

    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        SecureRandom secureRandom = new SecureRandom();

        // generate a random seed for the key generation
        byte[] seed = secureRandom.generateSeed(20);
        secureRandom.setSeed(seed);

        generator.initialize(2048, secureRandom);

        return generator.generateKeyPair();
    }

    public static String convertPrivateKeyToPEM(PrivateKey privateKey) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return formatPEM("PRIVATE KEY", encoder.encodeToString(keyFactory.generatePrivate(spec).getEncoded()));
    }

    public static String convertPublicKeyToPEM(PublicKey publicKey) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return formatPEM("PUBLIC KEY", encoder.encodeToString(keyFactory.generatePublic(spec).getEncoded()));
    }

    public static String formatPEM(String type, String content) {
        return "-----BEGIN " + type + "----- " +
                content +
                " -----END " + type + "-----";
    }

    public String convertKey(String key){
        return key.replace("\n", "");
    }

}
