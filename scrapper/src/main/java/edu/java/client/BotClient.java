package edu.java.client;

import edu.java.dto.bot.request.LinkUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

public class BotClient {
    private final RestClient restClient;
    private final RetryTemplate retryTemplate;

    public BotClient(String baseUrl, RetryTemplate retryTemplate) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Content-Type", "application/json")
            .build();
        this.retryTemplate = retryTemplate;
    }

    public ResponseEntity<?> sendUpdate(LinkUpdateRequest request) {
        return restClient.post()
            .uri("/updates")
            .body(request)
            .retrieve()
            .toBodilessEntity();
    }

    public ResponseEntity<?> sendUpdateRetry(LinkUpdateRequest request) {
        return retryTemplate.execute(
            retryContext -> sendUpdate(request)
        );
    }
}
