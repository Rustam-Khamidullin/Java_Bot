package edu.java.configuration.client;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;

@ConfigurationProperties(prefix = "api.github")
@RequiredArgsConstructor
public class GitHubClientConfiguration {
    @Autowired
    private final RetryTemplate retryTemplate;
    private final String baseUrl;

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubRestClient(baseUrl, retryTemplate);
    }
}
