package com.myproject.poc_structured_concurrency_with_resilience4J.dto;

public record BureauResponse(
        int score,
        boolean approved
) { }