

import MACC.ChatService.ChatServiceApplication;
import MACC.ChatService.model.Chat;
import MACC.ChatService.model.ChatService;
import MACC.ChatService.model.MappingService;
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

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ChatServiceApplication .class)
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private  ChatService chatService;
    @MockBean
    private  MappingService mappingService;



    @Test
    void initChatsPage() throws Exception {
        String sender = "test1";
        String username = "test2";
        Collection<Chat> chats= new ArrayList<>();
        when(chatService.getChats(any(String.class))).thenReturn(chats);
        mvc.perform(get("http://localhost:9004/chat")
                        .queryParam("sender",sender)
                        .header("Logged-In-User", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}




