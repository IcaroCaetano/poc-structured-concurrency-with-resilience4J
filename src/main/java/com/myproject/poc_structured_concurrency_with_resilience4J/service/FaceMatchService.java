// service/FaceMatchService.java

package com.myproject.poc_structured_concurrency_with_resilience4J.service;


import com.myproject.poc_structured_concurrency_with_resilience4J.config.ResilienceConfig;
import com.myproject.poc_structured_concurrency_with_resilience4J.dto.FaceMatchResponse;
import com.myproject.poc_structured_concurrency_with_resilience4J.util.LoggerUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class FaceMatchService {

    private final CircuitBreaker circuitBreaker =
            ResilienceConfig.createCircuitBreaker("face-match");

    private final Retry retry =
            ResilienceConfig.createRetry("face-match");

    private final AtomicInteger counter =
            new AtomicInteger();

    public FaceMatchResponse analyze(String cpf) {

        Supplier<FaceMatchResponse> supplier =
                Decorators.ofSupplier(() -> execute(cpf))
                        .withCircuitBreaker(circuitBreaker)
                        .withRetry(retry)
                        .decorate();

        return supplier.get();
    }

    private FaceMatchResponse execute(String cpf) {

        try {

            LoggerUtil.log("Calling Face Match");

            Thread.sleep(Duration.ofSeconds(1));

            int attempt = counter.incrementAndGet();

            if (attempt < 3) {

                LoggerUtil.log(
                        "Face Match FAILED - retrying..."
                );

                throw new RuntimeException(
                        "Temporary Face Match error"
                );
            }

            LoggerUtil.log("Face Match SUCCESS");

            return new FaceMatchResponse(
                    true,
                    0.98
            );

        } catch (InterruptedException e) {

            LoggerUtil.log("Face Match CANCELLED");

            throw new RuntimeException(e);
        }
    }
}