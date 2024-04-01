package edu.java.domain.jpa;

import edu.java.domain.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
    default ChatEntity saveIfNotExisting(Long tgChatId) {
        return findById(tgChatId).orElseGet(() -> {
            ChatEntity newChat = new ChatEntity();
            newChat.setId(tgChatId);
            return saveAndFlush(newChat);
        });
    }
}
