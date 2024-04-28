package edu.java.bot.client;

import edu.java.bot.dto.scrapper.request.AddLinkRequest;
import edu.java.bot.dto.scrapper.request.RemoveLinkRequest;
import edu.java.bot.dto.scrapper.response.LinkResponse;
import edu.java.bot.dto.scrapper.response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScrapperClient {
    private static final String TG_CHAT_URL = "/tg-chat/%d";
    private static final String LINKS_URL = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String INCORRECT_URL = "Incorrect URL";
    private final RetryTemplate retryTemplate;
    private final RestClient restClient;

    public ScrapperClient(@Value("${api.scrapper.baseUrl}") String baseUrl, RetryTemplate retryTemplate) {
        restClient = RestClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .baseUrl(baseUrl)
            .build();
        this.retryTemplate = retryTemplate;
    }

    public void registerChat(long id) {
        restClient.post()
            .uri(TG_CHAT_URL.formatted(id))
            .retrieve();
    }

    public void registerChatRetry(long id) {
        retryTemplate.execute(
            retryContext -> {
                registerChat(id);
                return null;
            }
        );
    }

    public void deleteChat(long id) {
        restClient.delete()
            .uri(TG_CHAT_URL.formatted(id))
            .retrieve();
    }

    public void deleteChatRetry(long id) {
        retryTemplate.execute(
            retryContext -> {
                deleteChat(id);
                return null;
            }
        );
    }

    public ListLinksResponse getAllLinks(long tgChatId) {
        return restClient.get()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .body(ListLinksResponse.class);
    }

    public ListLinksResponse getAllLinksRetry(long id) {
        return retryTemplate.execute(
            retryContext -> getAllLinks(id)
        );
    }

    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest)
        throws IllegalArgumentException {
        return restClient.post()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .body(addLinkRequest)
            .retrieve()
            .onStatus(status -> status == HttpStatus.BAD_REQUEST, (request, response) -> {
                throw new IllegalArgumentException(INCORRECT_URL);
            })
            .body(LinkResponse.class);
    }

    public LinkResponse addLinkRetry(long tgChatId, AddLinkRequest addLinkRequest) {
        return retryTemplate.execute(
            retryContext -> addLink(tgChatId, addLinkRequest)
        );
    }

    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest)
        throws IllegalArgumentException {
        return restClient.method(HttpMethod.DELETE)
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .body(removeLinkRequest)
            .retrieve()
            .onStatus(status -> status == HttpStatus.BAD_REQUEST, (request, response) -> {
                throw new IllegalArgumentException(INCORRECT_URL);
            })
            .onStatus(status -> status == HttpStatus.NOT_FOUND, (request, response) -> {
                throw new IllegalArgumentException("There is no such link");
            })
            .body(LinkResponse.class);
    }

    public LinkResponse removeLinkRetry(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return retryTemplate.execute(
            retryContext -> removeLink(tgChatId, removeLinkRequest)
        );
    }
}
