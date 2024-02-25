package edu.java.configuration;

import edu.java.configuration.client.GitHubClientConfiguration;
import edu.java.configuration.client.StackOverflowConfiguration;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableConfigurationProperties({GitHubClientConfiguration.class, StackOverflowConfiguration.class})
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }
}
