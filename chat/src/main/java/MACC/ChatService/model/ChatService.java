package MACC.ChatService.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ChatService {


    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public void saveChat(Chat chat) {chatRepository.save(chat);}

    public Collection<ChatMessage> getChatMessages(String sender, String receiver) {
        Collection<ChatMessage> ret = chatMessageRepository.findChat(sender, receiver);
        if (!Objects.isNull(ret)) {
            System.out.println("This is the content:" + ret);
            return ret;
        }
        return null;
    }

    public Collection<Chat> getChats(String sender) {
        System.out.println("This is the content:" + sender);
        Collection<Chat> ret = chatRepository.findChats(sender);
        if (!Objects.isNull(ret)) {
            System.out.println("This is the content:" + ret);
            return ret;
        }

        return null;
    }
}