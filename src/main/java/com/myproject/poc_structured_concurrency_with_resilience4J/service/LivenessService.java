package com.myproject.poc_structured_concurrency_with_resilience4J.service;

import com.myproject.poc_structured_concurrency_with_resilience4J.config.ResilienceConfig;
import com.myproject.poc_structured_concurrency_with_resilience4J.dto.LivenessResponse;
import com.myproject.poc_structured_concurrency_with_resilience4J.util.LoggerUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import java.time.Duration;
import java.util.function.Supplier;

public class LivenessService {

    private final CircuitBreaker circuitBreaker =
            ResilienceConfig.createCircuitBreaker("liveness");

    public LivenessResponse analyze(String cpf) {

        Supplier<LivenessResponse> supplier =
                CircuitBreaker.decorateSupplier(
                        circuitBreaker,
                        () -> execute(cpf)
                );

        return supplier.get();
    }

    private LivenessResponse execute(String cpf) {

        try {

            LoggerUtil.log("Calling Liveness");

            Thread.sleep(Duration.ofSeconds(2));

            LoggerUtil.log("Liveness SUCCESS");

            return new LivenessResponse(
                    true,
                    0.99
            );

        } catch (InterruptedException e) {

            LoggerUtil.log("Liveness CANCELLED");

            throw new RuntimeException(e);
        }
    }
}