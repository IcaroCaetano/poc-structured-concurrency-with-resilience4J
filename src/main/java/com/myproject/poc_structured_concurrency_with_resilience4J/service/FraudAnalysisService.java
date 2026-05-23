package com.myproject.poc_structured_concurrency_with_resilience4J.service;

import com.myproject.poc_structured_concurrency_with_resilience4J.dto.FraudAnalysisResponse;
import com.myproject.poc_structured_concurrency_with_resilience4J.util.LoggerUtil;

import java.util.concurrent.StructuredTaskScope;

public class FraudAnalysisService {

    private final FaceMatchService faceMatchService = new FaceMatchService();

    private final LivenessService livenessService = new LivenessService();

    private final BureauService bureauService =
            new BureauService();

    public FraudAnalysisResponse analyze(String cpf) {

        LoggerUtil.log("Starting Fraud Analysis");

        try (var scope = StructuredTaskScope.open()) {

            var faceTask = scope.fork(() -> faceMatchService.analyze(cpf));

            var livenessTask = scope.fork(() -> livenessService.analyze(cpf));

            var bureauTask = scope.fork(() -> bureauService.analyze(cpf));

            scope.join();

            LoggerUtil.log("All integrations completed");

            return new FraudAnalysisResponse(
                    faceTask.get(),
                    livenessTask.get(),
                    bureauTask.get()
            );

        } catch (Exception e) {

            LoggerUtil.log("Fraud Analysis FAILED");

            throw new RuntimeException(e);
        }
    }
}