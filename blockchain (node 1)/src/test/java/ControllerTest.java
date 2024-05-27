


import LAP.Blockchain.BlockchainApplication;
import LAP.Blockchain.dto.Payment;
import LAP.Blockchain.service.BlockchainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = BlockchainApplication .class)
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private  BlockchainService blockchainService;



    @Test
    void insertTransaction() throws Exception {
        Payment request = new Payment(10L,"test1", "test2","40","test3");
        when(blockchainService.addTransaction(any(Payment.class))).thenReturn(1);
        mvc.perform(
                        post("http://localhost:9012/blockchain/addTransaction")
                                .header("Logged-In-User", "user")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }




}




