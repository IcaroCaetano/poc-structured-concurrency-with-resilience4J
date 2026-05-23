package com.myproject.poc_structured_concurrency_with_resilience4J.util;

public class LoggerUtil {

    public static void log(String message) {

        var requestId = RequestContext.REQUEST_ID.isBound()
                ? RequestContext.REQUEST_ID.get()
                : "NO_REQUEST_ID";

        System.out.printf(
                "[requestId=%s] [thread=%s] %s%n",
                requestId,
                Thread.currentThread(),
                message
        );
    }
}