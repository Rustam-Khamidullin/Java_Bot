package edu.java.configuration.retry;

import java.util.Set;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpClientErrorException;

public class HttpRetryPolicy extends SimpleRetryPolicy {
    private final Set<Integer> retryableStatusCodes;

    public HttpRetryPolicy(int maxAttempts, Set<Integer> retryableStatusCodes) {
        super(maxAttempts);
        this.retryableStatusCodes = retryableStatusCodes;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        Throwable lastThrowable = context.getLastThrowable();
        if (lastThrowable == null) {
            return super.canRetry(context);
        }

        if (lastThrowable instanceof HttpClientErrorException ex) {
            return retryableStatusCodes.contains(ex.getStatusCode().value())
                && super.canRetry(context);
        }
        return false;
    }
}
