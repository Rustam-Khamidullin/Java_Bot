package edu.java.scrapper;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.net.URI;
import java.util.Set;

public class JdbcLinkTest extends IntegrationTest {
    private final DataSource dataSource =
        DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final JdbcLinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate);
    private final JdbcChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);

    @Test
    @Transactional
    @Rollback
    void simpleTest() {
        chatRepository.addOrGetExists(1L);
        chatRepository.addOrGetExists(2L);
        chatRepository.addOrGetExists(3L);
        chatRepository.addOrGetExists(3L);

        linkRepository.addOrGetExists(1L, URI.create("a"));
        linkRepository.addOrGetExists(1L, URI.create("a"));
        linkRepository.addOrGetExists(1L, URI.create("b"));
        linkRepository.addOrGetExists(1L, URI.create("c"));
        linkRepository.addOrGetExists(1L, URI.create("d"));
        linkRepository.addOrGetExists(2L, URI.create("c"));
        linkRepository.addOrGetExists(3L, URI.create("d"));

        var links = linkRepository.findAll();

        Assertions.assertEquals(links.size(), 6);
        for (var elem : links) {
            Assertions.assertTrue(Set.of("a", "b", "c", "d").contains(elem.url().toString()));
        }

        var chats = chatRepository.findAll();

        Assertions.assertEquals(chats.size(), 3);

        chatRepository.remove(1L);
        links = linkRepository.findAll();
        Assertions.assertEquals(links.size(), 2);

        linkRepository.remove(3L, URI.create("d"));
        links = linkRepository.findAll();
        Assertions.assertEquals(links.size(), 1);

        chats = chatRepository.findAll();
        Assertions.assertEquals(chats.size(), 2);
    }
}
