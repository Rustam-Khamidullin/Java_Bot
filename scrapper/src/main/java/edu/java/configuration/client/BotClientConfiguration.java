package edu.java.configuration.client;

import edu.java.client.BotClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "api.bot")
@RequiredArgsConstructor
public class BotClientConfiguration {
    private final String baseUrl;

    @Bean
    BotClient botClient() {
        return new BotClient(baseUrl);
    }
}
