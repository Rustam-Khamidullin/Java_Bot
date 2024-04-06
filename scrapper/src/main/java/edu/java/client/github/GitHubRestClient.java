package edu.java.client.github;

import edu.java.dto.github.Repository;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;

public class GitHubRestClient implements GitHubClient {
    private final RetryTemplate retryTemplate;
    private final RestClient restClient;

    public GitHubRestClient(String baseUrl, RetryTemplate retryTemplate) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Repository getRepository(String owner, String repository) {
        return retryTemplate.execute(
            retryContext -> restClient.get()
                .uri("repos/%s/%s".formatted(owner, repository))
                .retrieve()
                .body(Repository.class));
    }
}
