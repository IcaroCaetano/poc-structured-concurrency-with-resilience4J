package com.myproject.poc_structured_concurrency_with_resilience4J.context;

public final class RequestContext {

    private RequestContext() {
    }

    public static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
}