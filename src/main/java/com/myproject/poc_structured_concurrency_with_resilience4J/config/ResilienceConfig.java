package com.myproject.poc_structured_concurrency_with_resilience4J.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;

import java.time.Duration;

public class ResilienceConfig {

    private ResilienceConfig() {
    }

    public static CircuitBreaker createCircuitBreaker(String name) {

        return CircuitBreaker.ofDefaults(name);
    }

    public static Retry createRetry(String name) {

        return Retry.ofDefaults(name);
    }

    public static TimeLimiter createTimeLimiter() {

        return TimeLimiter.of(
                Duration.ofSeconds(2)
        );
    }
}