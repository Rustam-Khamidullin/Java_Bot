package edu.java.client;

import edu.java.dto.bot.request.LinkUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class BotClient {
    private final RestClient restClient;

    public BotClient(String baseUrl) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public ResponseEntity<?> sendUpdate(LinkUpdateRequest request) {
        return restClient.post()
            .uri("/updates")
            .body(request)
            .retrieve()
            .toBodilessEntity();
    }
}
