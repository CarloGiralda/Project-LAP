package LAP.Blockchain.service;


import LAP.Blockchain.dto.Block;
import LAP.Blockchain.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Service
@Slf4j
public class ProducerService {

    @Autowired
    private RabbitTemplate template;

    public void broadcastMessage(Block message) {
        this.template.convertAndSend("my-exchange", "", message);// broadcasts string message to each my-queue-* via my-exchange

    }
}