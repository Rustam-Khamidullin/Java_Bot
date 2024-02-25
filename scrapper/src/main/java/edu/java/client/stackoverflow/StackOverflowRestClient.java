package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Question;
import org.springframework.web.client.RestClient;

public class StackOverflowRestClient implements StackOverflowClient {
    private final RestClient restClient;

    public StackOverflowRestClient(String baseUrl) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public Question getQuestion(int id) {
        return restClient.get()
            .uri("questions/%d".formatted(id))
            .header("site", "stackoverflow")
            .retrieve()
            .body(Question.class);
    }
}
