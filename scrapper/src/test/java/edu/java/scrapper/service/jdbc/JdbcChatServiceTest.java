package edu.java.scrapper.service.jdbc;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.dto.repository.Chat;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatServiceTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Transactional
    @Rollback
    @Test
    public void testRegister() {
        JdbcChatService chatService = new JdbcChatService(jdbcChatRepository);

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
        JdbcChatService chatService = new JdbcChatService(jdbcChatRepository);

        long tgChatId = 123456L;
        Chat chat = chatService.register(tgChatId);

        Assertions.assertNotNull(chat);
        Assertions.assertNotNull(jdbcChatRepository.findByChatId(tgChatId));

        boolean result = chatService.unregister(tgChatId);
        Assertions.assertTrue(result);
        Assertions.assertNull(jdbcChatRepository.findByChatId(tgChatId));
    }
}
