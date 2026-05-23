# poc-structured-concurrency-with-resilience4j


````
Request
   |
   v
FraudAnalysisService
   |
   +--> Face Match
   |       ├── Retry
   |       └── CircuitBreaker
   |
   +--> Liveness
   |       └── Timeout
   |
   +--> Bureau
           ├── Retry
           ├── Timeout
           └── Fallback
````