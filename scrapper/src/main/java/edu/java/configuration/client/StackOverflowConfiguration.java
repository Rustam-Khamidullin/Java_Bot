package edu.java.configuration.client;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;

@ConfigurationProperties(prefix = "api.stackoverflow")
@RequiredArgsConstructor
public class StackOverflowConfiguration {
    @Autowired
    private final RetryTemplate retryTemplate;
    private final String baseUrl;

    @Bean
    StackOverflowClient stackOverflowClient() {
        return new StackOverflowRestClient(baseUrl, retryTemplate);
    }
}
