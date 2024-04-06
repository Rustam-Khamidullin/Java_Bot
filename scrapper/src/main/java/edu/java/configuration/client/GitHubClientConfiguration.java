package edu.java.configuration.client;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;

@ConfigurationProperties(prefix = "api.github")
@RequiredArgsConstructor
public class GitHubClientConfiguration {
    private final String baseUrl;
    private final RetryTemplate retryTemplate;

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubRestClient(baseUrl, retryTemplate);
    }
}
