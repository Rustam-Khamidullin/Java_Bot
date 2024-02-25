package edu.java.configuration;

import edu.java.configuration.client.GitHubClientConfiguration;
import edu.java.configuration.client.Scheduler;
import edu.java.configuration.client.StackOverflowConfiguration;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({
    GitHubClientConfiguration.class,
    StackOverflowConfiguration.class})
@ConfigurationProperties(prefix = "app.scheduler")
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final boolean enable;
    private final Duration interval;

    @Bean
    Scheduler scheduler() {
        return new Scheduler(enable, interval);
    }
}
