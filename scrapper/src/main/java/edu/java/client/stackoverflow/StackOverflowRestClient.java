package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Questions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class StackOverflowRestClient implements StackOverflowClient {
    private final RestClient restClient;

    public StackOverflowRestClient(String baseUrl) {
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("site", "stackoverflow")
                .build();
    }

    @Override
    public ResponseEntity<Questions> getQuestions(int id) {
        return restClient.get()
                .uri("/questions/%d".formatted(id))
                .retrieve()
                .toEntity(Questions.class);

    }
}
