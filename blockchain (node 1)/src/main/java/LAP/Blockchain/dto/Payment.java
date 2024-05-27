package LAP.Blockchain.dto;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;
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

    public Payment(Long id, String sender,String receiver,String price,String signature){
        this.id = id;
        this.sender=sender;
        this.receiver=receiver;
        this.price=price;
        this.signature=signature;
        this.validated=false;
    }

    public Payment(){

    }
    public boolean validateSignature() {

        try {
            String msg = sender + receiver + price;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedData = digest.digest(msg.getBytes(StandardCharsets.UTF_8));

            // IF THE SENDER IS "BLOCKCHAIN", THEN THE TRANSACTION IS A COINBASE TRANSACTION
            // SO IT IS SIGNED WITH THE RECEIVER PUBLIC KEY
            String publicKeyPEM;
            if (sender.equals("Blockchain")) {
                publicKeyPEM = receiver
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll(System.lineSeparator(), "")
                        .replace("-----END PUBLIC KEY-----", "");
            }
            // OTHERWISE IT IS A NORMAL TRANSACTION
            else {
                publicKeyPEM = sender
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll(System.lineSeparator(), "")
                        .replace("-----END PUBLIC KEY-----", "");
            }

            byte[] encoded = Base64.decode(publicKeyPEM);

            java.security.PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));

            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(hashedData);
            return sign.verify(Base64.decode(signature));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
