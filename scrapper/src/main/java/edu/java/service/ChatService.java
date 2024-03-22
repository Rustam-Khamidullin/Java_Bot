package edu.java.service;

import edu.java.dto.repository.Chat;

public interface ChatService {
    Chat register(long tgChatId);

    boolean unregister(long tgChatId);
}
