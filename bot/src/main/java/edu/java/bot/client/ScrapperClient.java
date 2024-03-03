package edu.java.bot.client;

import edu.java.bot.dto.scrapper.request.AddLinkRequest;
import edu.java.bot.dto.scrapper.request.RemoveLinkRequest;
import edu.java.bot.dto.scrapper.response.LinkResponse;
import edu.java.bot.dto.scrapper.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private final RestClient restClient;

    public ScrapperClient(String baseUrl) {
        restClient = RestClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .baseUrl(baseUrl)
            .build();
    }

    public ResponseEntity<?> registerChat(long id) {
        return restClient.post()
            .uri("/tg-chat/%d" .formatted(id))
            .retrieve()
            .toBodilessEntity();
    }

    public ResponseEntity<?> deleteChat(long id) {
        return restClient.delete()
            .uri("/tg-chat/%d" .formatted(id))
            .retrieve()
            .toBodilessEntity();
    }

    public ResponseEntity<ListLinksResponse> getAllLinks(long tgChatId) {
        return restClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(tgChatId))
            .retrieve()
            .toEntity(ListLinksResponse.class);
    }

    public ResponseEntity<LinkResponse> addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        return restClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(tgChatId))
            .body(addLinkRequest)
            .retrieve()
            .toEntity(LinkResponse.class);
    }

    public ResponseEntity<LinkResponse> removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return restClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(tgChatId))
            .body(removeLinkRequest)
            .retrieve()
            .toEntity(LinkResponse.class);
    }
}
