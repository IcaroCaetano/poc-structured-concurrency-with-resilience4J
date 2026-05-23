package com.myproject.poc_structured_concurrency_with_resilience4J;

import com.myproject.poc_structured_concurrency_with_resilience4J.context.RequestContext;
import com.myproject.poc_structured_concurrency_with_resilience4J.service.FraudAnalysisService;

import java.util.UUID;

public class PocStructuredConcurrencyWithResilience4JApplication {

	static void main(String[] args) {

		var requestId = UUID.randomUUID().toString();

		ScopedValue.where(RequestContext.REQUEST_ID, requestId)
				.run(() -> {

					var fraudAnalysisService = new FraudAnalysisService();

					var response = fraudAnalysisService.analyze("12345678900");

					System.out.println("\nFINAL RESPONSE");
					System.out.println(response);
				});
	}

}
