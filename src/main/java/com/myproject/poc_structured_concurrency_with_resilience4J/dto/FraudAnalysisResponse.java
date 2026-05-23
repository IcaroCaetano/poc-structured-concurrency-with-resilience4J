package com.myproject.poc_structured_concurrency_with_resilience4J.dto;

public record FraudAnalysisResponse(
        FaceMatchResponse faceMatch,
        LivenessResponse liveness,
        BureauResponse bureau
) { }