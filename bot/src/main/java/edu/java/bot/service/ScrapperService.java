package edu.java.bot.service;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.scrapper.request.AddLinkRequest;
import edu.java.bot.dto.scrapper.request.RemoveLinkRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {
    private final ScrapperClient scrapperClient;

    public void register(long id) {
        scrapperClient.registerChat(id);
    }

    public void delete(long id) {
        scrapperClient.deleteChat(id);
    }

    public List<String> getTrackingLink(long tgChatId) {
        return scrapperClient.getAllLinks(tgChatId).links().stream()
            .map(e -> e.url().toString()).toList();
    }

    public boolean trackLink(long tgChatId, String link) {
        try {
            scrapperClient.addLink(
                tgChatId,
                new AddLinkRequest(link)
            );
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean removeLink(long tgChatId, String link) {
        try {
            scrapperClient.removeLink(
                tgChatId,
                new RemoveLinkRequest(link)
            );
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
