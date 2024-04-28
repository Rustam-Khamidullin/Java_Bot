package edu.java.bot.configuration.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RateLimitManager {
    private final int capacity;
    private final int refillAmount;
    private final Duration refillTime;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::createBucket);
    }

    private Bucket createBucket(String ip) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(capacity)
            .refillIntervally(refillAmount, refillTime)
            .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
