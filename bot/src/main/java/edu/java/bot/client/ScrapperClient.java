package edu.java.bot.client;

import edu.java.bot.dto.scrapper.request.AddLinkRequest;
import edu.java.bot.dto.scrapper.request.RemoveLinkRequest;
import edu.java.bot.dto.scrapper.response.LinkResponse;
import edu.java.bot.dto.scrapper.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

public class ScrapperClient {
    private static final String TG_CHAT_URL = "/tg-chat/%d";
    private static final String LINKS_URL = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final RetryTemplate retryTemplate;
    private final RestClient restClient;

    public ScrapperClient(String baseUrl, RetryTemplate retryTemplate) {
        restClient = RestClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .baseUrl(baseUrl)
            .build();
        this.retryTemplate = retryTemplate;
    }

    public void registerChat(long id) {
        retryTemplate.execute(retryContext -> restClient.post()
            .uri(TG_CHAT_URL.formatted(id))
            .retrieve());
    }

    public void deleteChat(long id) {
        retryTemplate.execute(retryContext -> restClient.delete()
            .uri(TG_CHAT_URL.formatted(id))
            .retrieve());
    }

    public ListLinksResponse getAllLinks(long tgChatId) {
        return retryTemplate.execute(retryContext -> restClient.get()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .body(ListLinksResponse.class));
    }

    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        return retryTemplate.execute(retryContext -> restClient.post()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .body(addLinkRequest)
            .retrieve()
            .body(LinkResponse.class));
    }

    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return retryTemplate.execute(retryContext -> restClient.method(HttpMethod.DELETE)
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .body(removeLinkRequest)
            .retrieve()
            .body(LinkResponse.class));
    }
}
