package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.dto.repository.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Chat register(long tgChatId) {
        return chatRepository.addOrGetExists(tgChatId);
    }

    public boolean unregister(long tgChatId) {
        return chatRepository.remove(tgChatId);
    }
}
