package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.dto.repository.Chat;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public Chat register(long tgChatId) {
        return chatRepository.addOrGetExisting(tgChatId);
    }

    @Override
    public boolean unregister(long tgChatId) {
        return chatRepository.remove(tgChatId);
    }
}
