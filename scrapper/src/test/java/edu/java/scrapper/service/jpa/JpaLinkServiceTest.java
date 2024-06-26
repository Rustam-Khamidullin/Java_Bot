package edu.java.scrapper.service.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.dto.repository.Link;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jpa.JpaLinkService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaLinkServiceTest extends IntegrationTest {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @AfterEach
    void cleanUp() {
        jpaChatRepository.deleteAll();
        jpaLinkRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testAdd() {
        JpaLinkService linkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);

        long tgChatId = 123456L;
        URI url = URI.create("https://example.com");

        Link link = linkService.add(tgChatId, url);

        Assertions.assertNotNull(link);
        Assertions.assertNotNull(link.linkId());
        Assertions.assertEquals(url, link.url());
        Assertions.assertEquals(tgChatId, link.chatId());
        Assertions.assertNotNull(link.lastUpdate());
    }

    @Transactional
    @Test
    public void testRemove() {
        JpaLinkService linkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);

        long tgChatId = 123456L;
        URI url = URI.create("https://example.com");

        var link = linkService.add(tgChatId, url);

        Link removedLink = linkService.remove(tgChatId, url);

        Assertions.assertNotNull(removedLink);
        Assertions.assertEquals(link.linkId(), removedLink.linkId());
        Assertions.assertEquals(url, removedLink.url());
        Assertions.assertEquals(tgChatId, removedLink.chatId());
        Assertions.assertNotNull(removedLink.lastUpdate());
    }

    @Transactional
    @Test
    public void testListAll() {
        JpaLinkService linkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);

        long tgChatId = 123456L;
        URI url1 = URI.create("https://example1.com");
        URI url2 = URI.create("https://example2.com");

        var link1 = linkService.add(tgChatId, url1);
        var link2 = linkService.add(tgChatId, url2);

        List<Link> allLinks = linkService.listAll(tgChatId);

        Assertions.assertNotNull(allLinks);
        Assertions.assertEquals(2, allLinks.size());
        Assertions.assertTrue(allLinks.stream().anyMatch(l -> l.linkId().equals(link1.linkId())));
        Assertions.assertTrue(allLinks.stream().anyMatch(l -> l.linkId().equals(link2.linkId())));
    }
}
