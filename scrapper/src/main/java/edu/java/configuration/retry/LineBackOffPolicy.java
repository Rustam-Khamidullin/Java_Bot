package edu.java.configuration.retry;

import lombok.Getter;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

public class LineBackOffPolicy implements BackOffPolicy {
    private final long initialInterval;
    private final long increment;

    public LineBackOffPolicy(long initialInterval) {
        this.initialInterval = initialInterval;
        this.increment = initialInterval;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return new LineBackOffContext(initialInterval);
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LineBackOffContext context = (LineBackOffContext) backOffContext;

        try {
            Thread.sleep(context.getInterval());
            context.incrementInterval(increment);
        } catch (InterruptedException e) {
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    @Getter
    static class LineBackOffContext implements BackOffContext {
        private long interval;

        LineBackOffContext(long interval) {
            this.interval = interval;
        }

        public void incrementInterval(long increment) {
            this.interval += increment;
        }
    }
}
