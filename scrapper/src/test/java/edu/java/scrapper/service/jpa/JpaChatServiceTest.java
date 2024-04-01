package edu.java.scrapper.service.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.dto.repository.Chat;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jpa.JpaChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Transactional
    @Rollback
    @Test
    public void testRegister() {
        JpaChatService chatService = new JpaChatService(jpaChatRepository);

        long tgChatId = 123456L;

        Chat chat = chatService.register(tgChatId);

        Assertions.assertNotNull(chat);
        Assertions.assertNotNull(chat.chatId());
        Assertions.assertNotNull(chat.createdAt());

        chat = chatService.register(tgChatId);

        Assertions.assertNotNull(chat);
        Assertions.assertNotNull(chat.chatId());
        Assertions.assertNotNull(chat.createdAt());
    }

    @Transactional
    @Rollback
    @Test
    public void testUnregister() {
        JpaChatService chatService = new JpaChatService(jpaChatRepository);

        long tgChatId = 123456L;
        Chat chat = chatService.register(tgChatId);

        Assertions.assertNotNull(chat);
        Assertions.assertTrue(jpaChatRepository.existsById(tgChatId));

        boolean result = chatService.unregister(tgChatId);
        Assertions.assertTrue(result);
        Assertions.assertFalse(jpaChatRepository.existsById(tgChatId));
    }
}
