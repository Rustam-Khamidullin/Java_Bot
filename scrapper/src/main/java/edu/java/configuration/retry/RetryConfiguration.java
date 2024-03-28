package edu.java.configuration.retry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry")
public record RetryConfiguration(
    @NotBlank
    String strategy,
    @NotNull
    int maxAttempt,
    @NotNull
    long delay,
    int multiplier,
    @NotEmpty
    List<Integer> retryableStatusCodes) {
    @Bean
    RetryTemplate retryTemplate() {
        SimpleRetryPolicy retryPolicy = new HttpRetryPolicy(maxAttempt, new HashSet<>(retryableStatusCodes));

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);

        switch (strategy) {
            case "const" -> {
                FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
                backOffPolicy.setBackOffPeriod(delay);
                retryTemplate.setBackOffPolicy(backOffPolicy);
            }
            case "line" -> {
                LineBackOffPolicy backOffPolicy = new LineBackOffPolicy(delay);
                retryTemplate.setBackOffPolicy(backOffPolicy);
            }
            case "exp" -> {
                ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
                backOffPolicy.setInitialInterval(delay);
                backOffPolicy.setMultiplier(multiplier);
                retryTemplate.setBackOffPolicy(backOffPolicy);
            }
            default -> throw new RuntimeException();
        }

        return retryTemplate;
    }
}
