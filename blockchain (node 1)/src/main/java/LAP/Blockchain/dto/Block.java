package LAP.Blockchain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Slf4j
public class Block implements Serializable {
    private String hash;
    private String previousHash;
    private ArrayList<Payment> data;
    private long timeStamp;
    private long nonce;

    public Block() {
        this.data = new ArrayList<>();
        this.timeStamp = Instant.now().toEpochMilli();
        this.nonce = 0L;
    }

    public Block(String previousHash) {
        this.data = new ArrayList<>();
        this.previousHash = previousHash;
        this.timeStamp = Instant.now().toEpochMilli();
        this.nonce = 0L;
        this.hash = calculateBlockHash();
    }

    public Block(String previousHash, Payment data) {
        this.data = new ArrayList<>();
        this.data.add(data);
        this.previousHash = previousHash;
        this.timeStamp = Instant.now().toEpochMilli();
        this.nonce = 0L;
        this.hash = calculateBlockHash();
    }

    public String calculateBlockHash() {
        StringBuilder datas = new StringBuilder();
        if (data.isEmpty()) {
            datas = new StringBuilder(data.toString());
        } else {
            for (Payment payment : data) {
                datas.append(payment.getPrice());
            }
        }
        String dataToHash = previousHash
                + Long.toString(timeStamp)
                + Long.toString(nonce)
                + datas ;

        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            log.info(ex.getMessage(), Level.SEVERE);
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    public String singleMine() {
        nonce++;
        hash = calculateBlockHash();
        return hash;
    }

    public void addToBlock(Payment trx) {
        this.data.add(trx);
    }
}
