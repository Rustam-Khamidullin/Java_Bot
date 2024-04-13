package edu.java.configuration.ratelimit;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@ConfigurationProperties(prefix = "bucket")
public class BucketConfiguration {
    int capacity;
    int refillAmount;
    Duration refillTime;

    @Bean
    RateLimitManager rateLimitManager() {
        return new RateLimitManager(capacity, refillAmount, refillTime);
    }
}

