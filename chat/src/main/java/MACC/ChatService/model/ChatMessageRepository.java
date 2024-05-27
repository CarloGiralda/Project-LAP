package MACC.ChatService.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    @Transactional
    @Query(value = "SELECT * FROM messages u WHERE u.sender = ?1 AND u.receiver = ?2 OR u.sender = ?2 AND u.receiver = ?1",nativeQuery = true)
    Collection<ChatMessage> findChat(@Param("sender") String sender,
                   @Param("receiver") String  receiver);

}
