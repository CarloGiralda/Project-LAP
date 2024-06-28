package LAP.Blockchain.service;

import LAP.Blockchain.dto.Block;
import LAP.Blockchain.dto.Payment;
import LAP.Blockchain.model.*;
import LAP.Blockchain.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

@PropertySource("classpath:application.properties")
@Service
@Slf4j
public class BlockchainService {
    private boolean found;
    private Block actualBlock;
    private Block nextBlock;
    private final Blockchain blockchain;
    private final ProducerService producerService;
    private boolean start;
    private final String public_key;
    private final String private_key;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClientService discoveryClientService;

    public BlockchainService(Blockchain blockchain, ProducerService producerService) {
        this.found = false;
        this.blockchain = blockchain;
        this.producerService = producerService;
        this.start = true;
        this.actualBlock = new Block();
        this.public_key = Constants.publicKey;
        this.private_key = Constants.privateKey;
    }

    public int addTransaction(Payment payment) {
        try {
            log.error("BLOCKCHAIN_SERVICE RECEIVES PAYMENT FROM THE OTHER NODE: sender: {}price: {}receiver: {}", payment.getSender(), payment.getPrice(), payment.getReceiver());
            // VALIDATE SIGNATURE
            if (!(payment.validateSignature())) {
                log.error("BLOCKCHAIN_SERVICE SIGNATURE VALIDATION FAILED");
                return 0;
            }
            log.error("BLOCKCHAIN_SERVICE SIGNATURE VALIDATION SUCCESS");
            // VALIDATE TRANSACTION
            if (!(validateTransaction(payment))) {
                log.error("BLOCKCHAIN_SERVICE TRANSACTION VALIDATION FAILED");
                return 0;
            }
            log.error("BLOCKCHAIN_SERVICE TRANSACTION VALIDATION SUCCESS");
            // ADD TRANSACTION TO BLOCK TO MINE
            nextBlock.addToBlock(payment);
            // BROADCAST TRANSACTION TO NODES
            if (!payment.isValidated()) {
                // BROADCAST TO ALL THE DUMMY BLOCK TRANSACTION
                Block block = new Block("/");
                ArrayList<Payment> ap = new ArrayList<>();
                payment.setValidated(true);
                ap.add(payment);
                block.setData(ap);
                producerService.broadcastMessage(block);
            }

            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public ArrayList<Block> retrieveBlockchain() throws JsonProcessingException {

        String blockchainUrl = discoveryClientService.getServiceUrl("BLOCKCHAIN-SERVICE");
        String blockchainRetrieveUrl = blockchainUrl + "/blockchain/getBlockchain";

        // REST call to book service to know car availability
        ResponseEntity<ArrayList> blockchainResponseEntity = restTemplate.getForEntity(blockchainRetrieveUrl, ArrayList.class);
        String blockchain = new ObjectMapper().writeValueAsString(blockchainResponseEntity.getBody());
        ArrayList<Block> blocks = new ObjectMapper().readValue(blockchain, new TypeReference<ArrayList<Block>>() {});
        if (blockchainResponseEntity.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("error, cannot check car validity");
        }

        return blocks;


        /*List<String> nodeIPs = discoveryClientService.getServiceInstances("BLOCKCHAIN-SERVICE");
        SocketClient socketClient = new SocketClient(nodeIPs.get(0), 5001);
        SocketServer socketServer = new SocketServer(5001);*/
        // TODO after this, the connection happens on this socket
    }

    private List<ArrayList<Block>> branchesWithMaxLength(List<ArrayList<Block>> branches) {
        ArrayList<ArrayList<Block>> max_branches = new ArrayList<>();
        int max = 0;
        for (ArrayList<Block> branch : branches) {
            if (branch.size() > max) {
                max_branches.clear();
                max = branch.size();
                max_branches.add(branch);
            } else if (branch.size() == max) {
                max_branches.add(branch);
            }
        }
        return max_branches;
    }

    private void branchEvaluation(List<ArrayList<Block>> max_branches) {
        if (max_branches.size() == 1) {
            Collections.reverse(max_branches.get(0));
            actualBlock.setPreviousHash(max_branches.get(0).get(max_branches.get(0).size() - 1).getHash());
            //blockchain.setBlocks(max_branches);
            for (Payment p : actualBlock.getData()) {
                if (!validateTransaction(p)) {
                    actualBlock.getData().remove(p);
                }
            }
        } else {
            boolean isPreviousHashOfActualBlockInOneBlockchain = false;
            if (actualBlock.getPreviousHash() != null) {
                for (ArrayList<Block> branch : max_branches) {
                    Collections.reverse(branch);
                    if (actualBlock.getPreviousHash().equals(branch.get(branch.size() - 1).getHash())) {
                        isPreviousHashOfActualBlockInOneBlockchain = true;
                    }
                }
            }
            if (!isPreviousHashOfActualBlockInOneBlockchain) {
                actualBlock.setPreviousHash(max_branches.get(0).get(max_branches.get(0).size() - 1).getHash());
                for (Payment p : actualBlock.getData()) {
                    if (!validateTransaction(p)) {
                        actualBlock.getData().remove(p);
                    }
                }
            }
        }
    }

    private void setPreviousHashOfLongestBranch() {
        List<ArrayList<Block>> branches = blockchain.findBranches();
        List<ArrayList<Block>> max_branches = branchesWithMaxLength(branches);
        branchEvaluation(max_branches);
    }

    private String createSignature(String privKey, String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedData = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            String privateKeyPEM = privKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] keyBytes = Base64.decode(privateKeyPEM.getBytes("utf-8"));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(keySpec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(hashedData);
            byte[] signedHash = signature.sign();

            return Base64.toBase64String(signedHash);
        } catch (Exception e) {
            log.error("BLOCKCHAIN_SERVICE SIGNATURE SIGNATURE FAILED");
            log.error(e.getMessage());
            return null;
        }
    }

    private void addCoinbaseTransaction() {
        String price = "50";
        // THE SIGNATURE IS PERFORMED WITH THE RECEIVER PRIVATE KEY BECAUSE IT IS A COINBASE TRANSACTION
        String signature = createSignature(this.private_key, "Blockchain" + this.public_key + price);
        Payment coinbase = new Payment(0L, "Blockchain", this.public_key, price, signature);
        actualBlock.getData().add(coinbase);
    }

    @Scheduled(fixedDelay = 10, initialDelay = 20000)
    public void mineBlock() throws JsonProcessingException {

        if (start) {
            blockchain.setBlocks(retrieveBlockchain());
            start = false;
        }
        found = false;

        // CREATE NEXT BLOCK
        nextBlock = new Block();

        // SET THE PREVIOUS HASH OF THE ACTUAL BLOCK AS THE HASH OF THE LAST BLOCK OF THE LONGEST CHAIN
        setPreviousHashOfLongestBranch();
        addCoinbaseTransaction();

        // DIFFICULTY
        // PREFIX
        int prefix = 5;
        // START MINING
        String prefixString = new String(new char[prefix]).replace('\0', '0');

        String hash;
        int count = 0;
        do {
            if (count == 50000) {
                count = 0;
                setPreviousHashOfLongestBranch();
            }
            count++;

            hash = actualBlock.singleMine();
        } while (!hash.substring(0, prefix).equals(prefixString) && !found);

        if (!found) {
            // NOTIFY THE OTHER NODE ABOUT THE HASH YOU FOUND
            producerService.broadcastMessage(actualBlock);
            log.error("BLOCKCHAIN_SERVICE NOTIFIES THE OTHER NODE ABOUT THE HASH FOUND");

            // IN BOTH CASES, THE BLOCK IS ADDED TO THE BLOCKCHAIN
            blockchain.addBlock(actualBlock);
            log.error("BLOCKCHAIN_SERVICE ADDS BLOCK: {} TO THE BLOCKCHAIN", actualBlock.getHash());
        }

        actualBlock = nextBlock;
    }

    private boolean checkForMaliciousTransactions(ArrayList<Payment> trxs) {
        boolean coinbaseTransaction = false;
        for (Payment trx : trxs) {
            if (!trx.validateSignature() || !validateTransaction(trx)) {
                return true;
            }
            if (trx.getSender().equals("Blockchain") && !coinbaseTransaction) {
                coinbaseTransaction = true;
            } else if (trx.getSender().equals("Blockchain") && coinbaseTransaction) {
                return true;
            }
        }
        if (!coinbaseTransaction) {
            return true;
        }
        return false;
    }

    public void checkBlock(Block block) {
        if (checkForMaliciousTransactions(block.getData())) {
            log.error("BLOCKCHAIN_SERVICE MALICIOUS TRANSACTIONS FOUND");
            return;
        }
        String hash = block.calculateBlockHash();
        if (hash.equals(block.getHash())) {
            found = true;
            blockchain.addBlock(block);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void logging() {
        ArrayList<String> logs = new ArrayList<>();
        for (Block block : blockchain.getBlocks()) {
            StringBuilder d = new StringBuilder();
            if (block.getData().isEmpty()) {
                d = new StringBuilder(block.getData().toString());
            } else {
                for (Payment trx : block.getData()) {
                    if (trx.getSender().equals("Blockchain") || trx.getSender().equals("Genesis Block")) {
                        d.append(trx.getSender());
                    } else {
                        String sender = trx.getSender().replace("-----BEGIN PUBLIC KEY-----", "");
                        d.append(sender, 44, 55);
                    }
                    d.append(",");
                    String receiver = trx.getReceiver().replace("-----BEGIN PUBLIC KEY-----", "");
                    d.append(receiver, 44, 55);
                    d.append(",");
                    d.append(trx.getPrice());
                    d.append("---");
                }
            }
            logs.add("[BLOCK: " + block.getHash().substring(0, 10) + " TRANSACTIONS: " + d + "]-->\n");
        }
        log.error("\n-----BLOCKCHAIN-----\n"+logs);
    }

    private ArrayList<Block> checkBranchOfTransaction(Payment payment) {
        List<ArrayList<Block>> branches = blockchain.findBranches();
        for (ArrayList<Block> branch : branches) {
            for (Block block : branch) {
                if (block.getData().contains(payment)) {
                    return branch;
                }
            }
        }
        return null;
    }

    public boolean validateTransaction(Payment trx) {
        if (trx.getSender().equals("Blockchain")) {
            return true;
        }
        double balance = 0;
        String sender_pk = trx.getSender();
        // FIND THE BRANCH THAT CONTAINS THE TRANSACTION
        ArrayList<Block> branch = checkBranchOfTransaction(trx);
        // IF NO BRANCH CONTAINS IT, THEN IT IS BEING ADDED RIGHT NOW
        // SO ALSO THE OTHER TRANSACTIONS IN THE SAME BLOCK MUST BE CHECKED
        if (branch == null) {
            for (Block block : blockchain.getBlocks()) {
                for (Payment payment : block.getData()) {
                    if (payment.getSender().equals(sender_pk)) {
                        balance -= Double.parseDouble(payment.getPrice());
                    }
                    if (payment.getReceiver().equals(sender_pk)) {
                        balance += Double.parseDouble(payment.getPrice());
                    }
                }
            }
            for (Payment payment : actualBlock.getData()) {
                if (payment.getSender().equals(sender_pk)) {
                    balance -= Double.parseDouble(payment.getPrice());
                }
                if (payment.getReceiver().equals(sender_pk)) {
                    balance += Double.parseDouble(payment.getPrice());
                }
            }
        }
        // OTHERWISE A BRANCH IS FOUND
        else {
            for (Block block : branch) {
                for (Payment payment : block.getData()) {
                    // DO NOT CONSIDER THE TRANSACTION IF IT IS EXACTLY THE ONE WE ARE COMPUTING
                    if (payment.equals(trx)) {
                        continue;
                    }
                    if (payment.getSender().equals(sender_pk)) {
                        balance -= Double.parseDouble(payment.getPrice());
                    }
                    if (payment.getReceiver().equals(sender_pk)) {
                        balance += Double.parseDouble(payment.getPrice());
                    }
                }
                // ITERATE UNTIL THE BLOCK CONTAINS THE TRANSACTION
                // SUBSEQUENT BLOCKS MUST NOT BE SCANNED
                if (block.getData().contains(trx)) {
                    break;
                }
            }
        }
        return !(balance < Double.parseDouble(trx.getPrice()));
    }
}
