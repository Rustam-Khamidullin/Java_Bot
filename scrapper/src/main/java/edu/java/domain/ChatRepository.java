package edu.java.domain;

import edu.java.dto.repository.Chat;
import java.util.List;

public interface ChatRepository {
    Chat addOrGetExists(Long chatId);

    boolean remove(Long chatId);

    List<Chat> findAll();

    Chat findByChatId(Long chatId);
}

