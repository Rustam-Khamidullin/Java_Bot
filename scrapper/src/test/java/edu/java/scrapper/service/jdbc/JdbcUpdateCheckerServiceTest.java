package edu.java.scrapper.service.jdbc;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.LinkUpdateCheckerService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdateCheckerService;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcUpdateCheckerServiceTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Transactional
    @SneakyThrows
    @Rollback
    @Test
    void getUnupdatedTest() {
        JdbcLinkService linkService = new JdbcLinkService(jdbcLinkRepository, jdbcChatRepository);
        LinkUpdateCheckerService linkUpdateCheckerService =
            new JdbcLinkUpdateCheckerService(jdbcLinkRepository);

        Timestamp start = new Timestamp(Instant.now().toEpochMilli());

        linkService.add(1, URI.create("test1"));
        linkService.add(2, URI.create("test2"));
        linkService.add(3, URI.create("test3"));

        Thread.sleep(1000);

        Timestamp end = new Timestamp(Instant.now().toEpochMilli());

        var links = linkUpdateCheckerService.getUnupdatedLinks(end);
        Assertions.assertEquals(links.size(), 3);

        links = linkUpdateCheckerService.getUnupdatedLinks(start);
        Assertions.assertEquals(links.size(), 0);
    }
}
