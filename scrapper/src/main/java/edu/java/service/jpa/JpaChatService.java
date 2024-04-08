package edu.java.service.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.dto.repository.Chat;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public Chat register(long tgChatId) {
        var chat = chatRepository.saveIfNotExisting(tgChatId);
        return new Chat(
            chat.getId(),
            chat.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public boolean unregister(long tgChatId) {
        var result = chatRepository.existsById(tgChatId);
        chatRepository.deleteById(tgChatId);
        return result;
    }
}
