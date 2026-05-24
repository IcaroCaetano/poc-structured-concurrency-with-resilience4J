# poc-structured-concurrency-with-resilience4j

## Objetivo

Esta POC demonstra como utilizar Structured Concurrency no OpenJDK 25 para coordenar chamadas concorrentes em um cenário realista de integração entre empresas.


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