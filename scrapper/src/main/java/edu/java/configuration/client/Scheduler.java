package edu.java.configuration.client;

import java.time.Duration;

public record Scheduler(
    boolean enable,
    Duration interval
) {
}
