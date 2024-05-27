package LAP.Blockchain.service;

import LAP.Blockchain.dto.Block;
import LAP.Blockchain.dto.Payment;
import LAP.Blockchain.model.Blockchain;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Service
@Slf4j
public class ConsumerService {
    private final BlockchainService blockchainService;

    public void handleMessage(byte[] message) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Block block = objectMapper.readValue(message, Block.class);
        // RECEIVE TRANSACTION FROM OTHERS
        log.error("RECEIVED BLOCK PREVIOUS HASH "+block.getPreviousHash());
        if (!block.getPreviousHash().equals("/")) { // THREAD EMPTY BLOCK
            log.error("THREAD EMPTY BLOCK NOT GENESIS "+block.getHash());
            blockchainService.checkBlock(block);
        } else if (block.getPreviousHash().equals("/")){ // TRANSACTION CONTAINER BLOCK
            log.error("TRANSACTION CONTAINER BLOCK "+block.getHash());
            blockchainService.addTransaction(block.getData().get(0));
        }
    }
}



