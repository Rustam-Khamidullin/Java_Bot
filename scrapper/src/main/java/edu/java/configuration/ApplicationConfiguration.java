package edu.java.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.configuration.ratelimit.BucketConfiguration;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
@EnableConfigurationProperties(BucketConfiguration.class)
@Data
public class ApplicationConfiguration {
    private final Scheduler scheduler;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Scheduler scheduler() {
        return scheduler;
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }
}
