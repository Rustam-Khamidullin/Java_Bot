package edu.java.component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandledCounter {
    private final Counter handledRequests;

    @Autowired
    public HandledCounter(MeterRegistry registry) {
        handledRequests = Counter.builder("handled_requests")
            .register(registry);
    }

    public void incrementCounter() {
        handledRequests.increment();
    }
}
