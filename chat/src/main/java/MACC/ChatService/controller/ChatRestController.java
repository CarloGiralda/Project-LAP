package MACC.ChatService.controller;

import MACC.ChatService.dto.ChatMessageDto;
import MACC.ChatService.model.ChatMessage;
import MACC.ChatService.model.ChatService;
import MACC.ChatService.model.MappingService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(path="/chat")
@AllArgsConstructor
public class ChatRestController {


    private final ChatService chatService;
    private final MappingService mappingService;


    @GetMapping("/messages/{sender}/{receiver}/{sessionId}")
    @ResponseBody
    public ResponseEntity<String> loadAllChat(@PathVariable String sender, @PathVariable String receiver, @PathVariable String sessionId, @RequestHeader("Logged-In-User") String username) {
        try {
            mappingService.updateSession(sender, sessionId);
            Collection<ChatMessage> ret=chatService.getChatMessages(sender,receiver);
            ArrayList<ChatMessageDto> r=new ArrayList<>();
            ChatMessageDto msg=new ChatMessageDto(0L,"The messages exchanged will be shown here","","", "", ChatMessageDto.MessageType.CHAT);
            r.add(msg);
            if (Objects.isNull(ret)) {
                return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(r));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(ret));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

    }

}





