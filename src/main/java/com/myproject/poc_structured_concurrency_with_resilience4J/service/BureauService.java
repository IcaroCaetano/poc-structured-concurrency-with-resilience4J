package com.myproject.poc_structured_concurrency_with_resilience4J.service;

import com.myproject.poc_structured_concurrency_with_resilience4J.config.ResilienceConfig;
import com.myproject.poc_structured_concurrency_with_resilience4J.dto.BureauResponse;
import com.myproject.poc_structured_concurrency_with_resilience4J.util.LoggerUtil;
import io.github.resilience4j.timelimiter.TimeLimiter;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class BureauService {

    private final TimeLimiter timeLimiter = ResilienceConfig.createTimeLimiter();

    public BureauResponse analyze(String cpf) {

        try {

            return timeLimiter.executeFutureSupplier(() ->
                    CompletableFuture.supplyAsync(
                            () -> execute(cpf),
                            Executors.newVirtualThreadPerTaskExecutor()
                    )
            );

        } catch (Exception e) {

            LoggerUtil.log("Bureau TIMEOUT - applying fallback");

            return fallback();
        }
    }

    private BureauResponse execute(String cpf) {

        try {

            LoggerUtil.log("Calling Bureau");

            Thread.sleep(Duration.ofSeconds(5));

            LoggerUtil.log("Bureau SUCCESS");

            return new BureauResponse(
                    850,
                    true
            );

        } catch (InterruptedException e) {

            LoggerUtil.log("Bureau CANCELLED");

            throw new RuntimeException(e);
        }
    }

    private BureauResponse fallback() {

        return new BureauResponse(
                300,
                false
        );
    }
}