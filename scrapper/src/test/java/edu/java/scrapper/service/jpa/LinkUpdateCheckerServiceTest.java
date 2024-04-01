package edu.java.scrapper.service.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.LinkUpdateCheckerService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdateCheckerService;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class LinkUpdateCheckerServiceTest extends IntegrationTest {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Transactional
    @SneakyThrows
    @Rollback
    @Test
    void getUnupdatedTest() {
        JpaLinkService linkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        LinkUpdateCheckerService linkUpdateCheckerService =
            new JpaLinkUpdateCheckerService(jpaLinkRepository);

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
