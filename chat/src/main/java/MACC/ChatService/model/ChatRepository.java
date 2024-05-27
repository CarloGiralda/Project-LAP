package MACC.ChatService.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Transactional
    @Query(value = "SELECT * FROM chats u WHERE u.sender = ?1 OR u.receiver= ?1",nativeQuery = true)
    Collection<Chat> findChats(@Param("sender") String sender);
}
