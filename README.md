# poc-structured-concurrency-with-resilience4j

## Objetivo

Esta POC demonstra como utilizar Structured Concurrency no OpenJDK 25 para coordenar chamadas concorrentes em um cenário realista de integração entre empresas.

O projeto simula um fluxo de análise antifraude que consulta múltiplos provedores externos em paralelo:

- Face Match
- Liveness
- Bureau Score

#### Além disso, a POC também demonstra:

- Virtual Threads
- Scoped Values
- Retry
- Circuit Breaker
- Timeout
- Fallback
- Propagação de contexto
Resiliência com Resilience4j

## Arquitetura  

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

### Cenário de negócio

Em plataformas de prevenção a fraude, uma única requisição normalmente depende de várias integrações externas.

Exemplo:

```
Fraud Analysis
    |
    +--> Face Match Provider
    +--> Liveness Provider
    +--> Bureau Provider
```

Cada integração:

- possui latência diferente,
- pode falhar,
- pode sofrer timeout,
- pode ficar indisponível.

O objetivo desta POC é demonstrar como o Java 25 resolve esse problema utilizando um modelo moderno de concorrência estruturada.

### Fluxo da aplicação

#### 1. A execução inicia na Main

A classe Main:

- gera um requestId,
- propaga o contexto usando ScopedValue,
- inicia o fluxo principal da análise antifraude.

Exemplo:

```java

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
```

#### 2. A FraudAnalysisService coordena todo o processamento

A FraudAnalysisService:

- abre o escopo estruturado,
- cria as subtarefas concorrentes,
- aguarda os resultados,
consolida as respostas.

```java

public class FraudAnalysisService {

    private final FaceMatchService faceMatchService = new FaceMatchService();

    private final LivenessService livenessService = new LivenessService();

    private final BureauService bureauService = new BureauService();

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
```