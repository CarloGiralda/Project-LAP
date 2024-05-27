package LAP.Blockchain.service;

import LAP.Blockchain.dto.Block;
import LAP.Blockchain.dto.Payment;
import LAP.Blockchain.model.*;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
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
    private final String public_key;
    private final String private_key;

    public BlockchainService(Blockchain blockchain, ProducerService producerService) {
        this.found = false;
        this.blockchain = blockchain;
        this.producerService = producerService;
        this.actualBlock = new Block();
        this.public_key = "-----BEGIN PUBLIC KEY-----" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzAzDBCwejYVjX7Vi3ZK3" +
                "Y0urENRSGqV+Wok1IUbg56bovq5VVJ0Kj0bEf6BGKyBOjzsyFFO6CGsBbSkWMuRs" +
                "GlLkfYFdduv2u0+TRYFCeTz7EOuIky3Wt0b9NqzQQ5z0zojFJCbDcPVoTvY5pEAc" +
                "qwakVM/kFURcxHTMcvstNHHYGdTocX/2Do9WAXYYARIjYGLyItyU0zbhDMVKw6GP" +
                "WSzUzlyBYPibU2X3xfJLHBf+/w3k8b2fS7C3rdjT1Dilt6SYt3xttr4Uc1q0eWVA" +
                "vtvPazynNJMOSUyXa0pmPdZ/G34aBkh+o/4fGrB4zE7+4X+t0gNDcBRZ85pOv4sX" +
                "swIDAQAB" +
                "-----END PUBLIC KEY-----";
        this.private_key = "-----BEGIN PRIVATE KEY-----" +
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDMDMMELB6NhWNf" +
                "tWLdkrdjS6sQ1FIapX5aiTUhRuDnpui+rlVUnQqPRsR/oEYrIE6POzIUU7oIawFt" +
                "KRYy5GwaUuR9gV126/a7T5NFgUJ5PPsQ64iTLda3Rv02rNBDnPTOiMUkJsNw9WhO" +
                "9jmkQByrBqRUz+QVRFzEdMxy+y00cdgZ1Ohxf/YOj1YBdhgBEiNgYvIi3JTTNuEM" +
                "xUrDoY9ZLNTOXIFg+JtTZffF8kscF/7/DeTxvZ9LsLet2NPUOKW3pJi3fG22vhRz" +
                "WrR5ZUC+289rPKc0kw5JTJdrSmY91n8bfhoGSH6j/h8asHjMTv7hf63SA0NwFFnz" +
                "mk6/ixezAgMBAAECggEBAIolcbrXa/AAiOD5WvAMLuCpEC4asWOc+8ir8C6RYB81" +
                "34mJWfgpQkbycIK/rgHwo39RYnxkmySr3ZFcKc1W3OGNkWhvNyoWYzs5ismZ07ll" +
                "Uc2IyTRr6ly3USO9KFs+XneeHWp2XAeT/oTHKwtK5J3dp1BMV20WlX/kW67U4ywH" +
                "We5qVyy3Syv2QLbWp4uHk4f+7nAvx1/PvFxcpPS0WkNtEi0gXBms7Wp/dwF/Mdyt" +
                "sz/m1xnWM7opzKzu/SrPWBXsjoroaQEnDWDAYXu8Yifs1se1pn7nlw5yKUaaoS51" +
                "4xblInBPLMKpLmVOYEFH7AY2t0UDK8bDW0wNi24UH8ECgYEA6VQf5IilzQqdrb+K" +
                "OZcNHALR5Rl8Z127QDfaj46q78bmwQ7euwnzCTmSuFmhGC8j+GO+oOFDGsjak+9i" +
                "vRuWkkb4n1c3D+WgFmFHjCsEodZYuQdBoKfrKxFyPWg4wm+Ni5ybqhNysvUYYbx8" +
                "zBO4fC3w5ziA8p9eYJHw/H+VSWMCgYEA3+BZjpJT3by0F05CszW3iTtH1NVpsEJz" +
                "GYVe2wBh9OIGn+pDtpyTLM/dUw64dN+jrYlyPF0dg13YGLJV29w6nuWqUkFLPZEk" +
                "jibbylc6lnWtr8Skni8+lHNUxiGObWom7J0az44cTmnG9U7Nzts6XIpBEqsghuCd" +
                "9eB/MbYLcXECgYEAqgJp2pLWfDgn+9l8VcoB+07ysQOI6agaMAiCb+d8FYnPyQuU" +
                "rqPpOeeBSGHGR2DRzMKgu4hm5LVEzlW2lCPt6ldcAH4D2Mlvg98NysvtbB5KN0da" +
                "Qj0X1SG5I5U1BjdR41AQ/DN/d3Wudkhohio4WgL/bPp2ulH06lhsRw6PMBECgYBi" +
                "wsbfNUAYDnnn2hdklWrc+DDM/ER+hCHgfUyGEhokfF1tyFuMwG/QkfZSAKlwrYO4" +
                "N20UogM2A30kD+/+aJ3XoPtNBA7cHx5gM1Y8YdSvsUQoyPfR7IeP13zrVrxNGkoH" +
                "kZrVOxzTnqgtaVEzUtThJABm6uUTex1T7XBPDr7nkQKBgQC/qGK3qK2sfGxIgAS/" +
                "T/hZ1C4ckn/9mlXAmDCd2B8DeE4uY5PZKm3Gb/Q06avCcrpYAL++/MNb/6AXaCVW" +
                "RI7dSnQgruFPiUF6bb6i7HUfi75HuOARc5NtIQn66W0TSsCdCVOzCrtcGa3k5lJr" +
                "wi6NhgM1e4Kz97jOmcwJa7UvqA==" +
                "-----END PRIVATE KEY-----";
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

    @Scheduled(fixedDelay = 10)
    public void mineBlock() {

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

    public ArrayList<Block> retrieveBlockchain() {
        return blockchain.getBlocks();
    }
}
