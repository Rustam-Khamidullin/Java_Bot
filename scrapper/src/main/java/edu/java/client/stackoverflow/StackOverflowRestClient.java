package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Questions;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

public class StackOverflowRestClient implements StackOverflowClient {
    private final RetryTemplate retryTemplate;
    private final RestClient restClient;

    public StackOverflowRestClient(String baseUrl, RetryTemplate retryTemplate) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Questions getQuestions(int id) {
        return retryTemplate.execute(
            retryContext -> restClient.get()
                .uri("/questions/%d".formatted(id))
                .retrieve()
                .body(Questions.class));
    }
}
