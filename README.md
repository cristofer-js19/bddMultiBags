# Multibags Loja Online
## INF-321 UNICAMP 2025 - Grupo 05


Resumo
- Projeto de testes BDD para o sistema multiBags (especificações em Gherkin).
- Objetivo: validar APIs e fluxos do domínio usando Cucumber + Rest‑Assured.

Principais características
- Features escritas em Gherkin (src/test/resources).
- Step definitions em Java (src/test/java/br/unicamp/inf321/vv).
- Validação de respostas JSON com JsonSchemaValidator (Rest‑Assured).
- Execução via Maven (Surefire) e/ou IDE.

Frameworks e libs principais
- Cucumber (io.cucumber:cucumber-java, cucumber-junit ou cucumber-junit-platform-engine)
- JUnit 4 ou JUnit 5 (dependendo da configuração do runner)
- Rest‑Assured (requisito: rest-assured + module-json-schema-validator)
- Maven (build e execução de testes)


## Como executar os testes:

1. Efetuar o clone do repositório:
```bash
git clone https://github.com/cristofer-js19/bddMultiBags.git
```

2. Acessar a pasta do projeto:
```bash
cd bddMultiBags
```

3. Executar os testes com o Maven:
```bash
mvn clean test
```


## Multibags Loja Online - URLs

* [MultiBags Website](http://multibags.1dt.com.br/shop/)
* [MultiBags Swagger](http://multibags.1dt.com.br/swagger-ui.html#/product-review-api)
