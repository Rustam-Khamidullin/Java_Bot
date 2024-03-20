package edu.java.client.github;

import edu.java.dto.github.Repository;
import org.springframework.web.client.RestClient;

public class GitHubRestClient implements GitHubClient {
    private final RestClient restClient;

    public GitHubRestClient(String baseUrl) {
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public Repository getRepository(String owner, String repository) {
        return restClient.get()
            .uri("repos/%s/%s".formatted(owner, repository))
            .retrieve()
            .body(Repository.class);
    }
}
