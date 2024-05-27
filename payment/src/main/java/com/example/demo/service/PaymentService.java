package com.example.demo.service;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.model.Block;
import com.example.demo.model.Credentials;
import com.example.demo.model.Payment;
import com.example.demo.repository.CredentialsRepo;
import com.example.demo.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.SysexMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class PaymentService {

    private final CredentialsRepo credentialsRepo;
    private final DiscoveryClientService discoveryClientService;

    private final KeyGenerator keyGenerator;
    private final RestTemplate restTemplate;

    private final List<Payment> transactions;

    private Long index;

    public PaymentService(CredentialsRepo credentialsRepo, DiscoveryClientService discoveryClientService, KeyGenerator keyGenerator, RestTemplate restTemplate) {
        this.credentialsRepo = credentialsRepo;
        this.discoveryClientService = discoveryClientService;
        this.keyGenerator = keyGenerator;
        this.restTemplate = restTemplate;
        this.transactions = new ArrayList<>();
        this.index = 0L;
    }

    private Credentials retrieveCredentials(String username) {
        return credentialsRepo.findByUsername(username);
    }


    public String createSignature(String privKey, String data) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedData = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        String privateKeyPEM = privKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] keyBytes = Base64.decode(privateKeyPEM.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = fact.generatePrivate(keySpec);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hashedData);
        byte[] signedHash = signature.sign();

        return Base64.toBase64String(signedHash);
    }

    public void sendTransaction(PaymentDTO paymentDTO) throws UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        Credentials sender = retrieveCredentials(paymentDTO.getSenderUsername());
        Credentials receiver = retrieveCredentials(paymentDTO.getReceiverUsername());

        if (sender == null || receiver == null) {
            throw new RuntimeException("sender or receiver is not stored");
        }

        // create transaction
        String signature = createSignature(sender.getPrivateKey(), sender.getPublicKey() + receiver.getPublicKey() + paymentDTO.getPrice());

        // create payment
        Payment payment = new Payment(index, sender.getPublicKey(), receiver.getPublicKey(), String.valueOf(paymentDTO.getPrice()), signature);

        if (!paymentDTO.getSenderUsername().equals("GIFTER")) {
            // save the transaction in memory
            saveTransaction(payment);
        }

        String blockchainUrl = discoveryClientService.getServiceUrl("BLOCKCHAIN-SERVICE");
        String transactionUrl = blockchainUrl + "/blockchain/addTransaction";

        ResponseEntity<String> blockchainResponseEntity = restTemplate.postForEntity(transactionUrl, payment, String.class);

        if (blockchainResponseEntity.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("error, transaction has been refused");
        }
    }

    private void saveTransaction(Payment payment){
        transactions.add(payment);
        index++; // TODO not scalable with many transactions!
    }


    @Scheduled(fixedRate = 25000)
    public void checkResponse(){
        try {

            Block[] blockchain = getBlockchain();

            boolean found;
            // scan the whole blockchain to check whether a transaction has been added correctly inside a block
            assert blockchain != null;
            for (Payment transaction: transactions) {
                found = false;
                for (Block block: blockchain) {
                    for(Payment payment: block.getData()) {
                        if(Objects.equals(transaction.getId(), payment.getId())) {
                            // advertise email service to notify user for the corresponding transaction
                            sendEmail(transaction);
                            // remove from stored transaction
                            transactions.remove(transaction);
                            found = true;
                            break;
                        }
                    }
                    // if the transaction has been found, skip to the next one
                    if (found) {
                        break;
                    }
                }
            }


        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }

    }

    public Block[] getBlockchain(){
        String blockchainUrl = discoveryClientService.getServiceUrl("BLOCKCHAIN-SERVICE");
        String getBlockchainUrl = blockchainUrl + "/blockchain/getBlockchain";

        // Get the blockchain
        ResponseEntity<Block[]> blockchainResponseEntity = restTemplate.getForEntity(getBlockchainUrl, Block[].class);

        // check response
        if (!blockchainResponseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("cannot get the blockchain");
        }

        // get the body
        return blockchainResponseEntity.getBody();
    }


    private void sendEmail(Payment transaction){
        // send request to email server
        String emailServiceUrl = discoveryClientService.getServiceUrl("SENDEMAIL-SERVICE") + "/email/sendPaymentEmail";
        log.info("Sending request to email service at {}", emailServiceUrl);

        // retrieve the username of sender and receiver from the public key
        String senderUsername = credentialsRepo.findCredentialsByPublicKey(transaction.getSender()).getUsername();
        String receiverUsername = credentialsRepo.findCredentialsByPublicKey(transaction.getReceiver()).getUsername();

        // create dto to send to email service
        PaymentDTO executedPayment = new PaymentDTO(senderUsername,receiverUsername, Long.parseLong(transaction.getPrice()));

        // make the request to email service
        ResponseEntity<String> response = restTemplate.postForEntity(emailServiceUrl, executedPayment, String.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("error in the email sender");
        }
    }


    public Long getUserBalance(String username) {
        try {
            // get blockchain
            Block[] blockchain = getBlockchain();

            // convert username to public key
            String senderPublicKey = retrieveCredentials(username).getPublicKey();

            long balance  = 0L;
            for (Block block : blockchain) {
                for (Payment payment : block.getData()) {

                    if (keyGenerator.convertKey(payment.getSender()).equals(senderPublicKey)) {
                        balance -= Long.parseLong(payment.getPrice());
                    }
                    if (keyGenerator.convertKey(payment.getReceiver()).equals(senderPublicKey)) {
                        balance += Long.parseLong(payment.getPrice());
                    }
                }
            }

            return balance;

        } catch (Exception e){
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public void generateWallet(String username) throws Exception {

        String[] keyPair = keyGenerator.generateKeyPair(username);

        // store key pair for the current user, 0 is private 1 is public key
        credentialsRepo.save(new Credentials(username, keyPair[0], keyPair[1]));

    }
}
