package edu.java.configuration.client;


import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "api.stackoverflow")
@RequiredArgsConstructor
public class StackOverflowConfiguration {
    private final String baseUrl;

    @Bean
    StackOverflowClient stackOverflowClient() {
        return new StackOverflowRestClient(baseUrl);
    }
}
