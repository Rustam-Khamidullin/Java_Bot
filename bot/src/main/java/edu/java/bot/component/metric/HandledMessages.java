package edu.java.bot.component.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandledMessages {
    private final Counter handledMessage;

    @Autowired
    public HandledMessages(MeterRegistry registry) {
        handledMessage = Counter.builder("handled_messages")
            .register(registry);
    }

    public void incrementCounter() {
        handledMessage.increment();
    }
}
