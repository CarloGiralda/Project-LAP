package MACC.ChatService.controller;

import MACC.ChatService.dto.ChatMessageDto;
import MACC.ChatService.model.Chat;
import MACC.ChatService.model.ChatMessage;
import MACC.ChatService.model.ChatService;
import MACC.ChatService.model.MappingService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MappingService mappingService;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;//remove if test




    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public ResponseEntity<String> initChatsPage(@RequestParam String sender, @RequestHeader("Logged-In-User") String username ) {

        try {
            Collection<Chat> chats= chatService.getChats(sender);
            Iterator<Chat> iterator = chats.iterator();
            while (iterator.hasNext()) {
                Chat it=iterator.next();
                if(Objects.equals(it.getReceiver(),sender)){
                    String temp=it.getSender();
                    it.setSender(it.getReceiver());
                    it.setReceiver(temp);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(chats));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

    }

/*
    @CrossOrigin("*")
    @RequestMapping(value = "/m", method = RequestMethod.GET)
    public String messages(Model model,@RequestParam String sender,@RequestParam String receiver) {
        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);
        System.out.println(sender+"------"+receiver);
        return "index";
    }


 */
    //@CrossOrigin("*")
    @MessageMapping("/secured/room")
    public void sendSpecific(@Payload ChatMessageDto msg)  {
        // Conversion of the message
        ChatMessage store=new ChatMessage();
        store.setSender(msg.getSender());
        store.setReceiver(msg.getReceiver());
        store.setContent(msg.getContent());
        store.setSessionId(msg.getSessionId());
        // Store the message
        chatService.saveChatMessage(store);
        // To same user
        // Update sender session ID
        mappingService.updateSession(msg.getSender(), msg.getSessionId());
        simpMessagingTemplate.convertAndSend("/secured/user/queue/specific-user-user"+msg.getSessionId(),msg);//remove if test
        // To receiver
        // Get receiver session ID
        String rSessionId= mappingService.getSession(msg.getReceiver());
        simpMessagingTemplate.convertAndSend("/secured/user/queue/specific-user-user"+rSessionId,msg);//remove if test

    }
}