package MACC.ChatService.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private String sessionId;
    private MessageType type;

    public enum MessageType {
        CHAT, LEAVE, JOIN
    }
}