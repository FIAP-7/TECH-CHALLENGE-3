# 3 - Setup projeto

## Prompt
> Perfeito, vamos fazer apenas algumas pequenas modificações. Primeiro, o projeto é um desafio da faculdade e não vai rodar na nuvem neste momento, apenas na maquina local de quem executar. Nesse contexto, faz sentido ter um API gateway? Se sim, como ele seria implementado?
> Segundo, faz sentido fazer todos os microservicos em um monorepo, separando em java module? Como ficaria a estrutura de pastas, considerando um projeto java spring com maven?

## Resposta
Excelente! São duas perguntas muito pertinentes que abordam os aspectos práticos da implementação e do gerenciamento do projeto em um ambiente de desenvolvimento local. Vamos detalhar cada um dos pontos.

## 1. O Papel do API Gateway em Desenvolvimento Local

**A resposta curta é: Sim, faz total sentido manter o API Gateway, mesmo para execução local.**

Embora não seja estritamente necessário para fazer a aplicação funcionar (você poderia chamar cada serviço em sua respectiva porta, como `localhost:8081`, `localhost:8082`, etc.), utilizá-lo traz benefícios que são centrais para o aprendizado e a correta implementação da arquitetura de microserviços.

**Por que manter o API Gateway?**

1.  **Ponto de Entrada Único (Single Point of Entry):** O cliente (seja o Postman, um frontend em React/Angular, etc.) só precisa conhecer um endereço: o do gateway (ex: `http://localhost:8080`). O gateway se encarrega de rotear a requisição para o microserviço correto (`localhost:8081`, `localhost:8082`, etc.). Isso simplifica drasticamente a configuração do cliente.

2.  **Centralização de Lógica Transversal (Cross-Cutting Concerns):** Este é o motivo mais importante. Lógicas que se aplicam a todas (ou quase todas) as requisições devem ficar no gateway, evitando duplicação de código nos outros microserviços. Para este projeto, o principal exemplo é a **validação do token JWT**.
    *   **Sem Gateway:** Cada serviço (`Agendamento`, `Histórico`) teria que implementar a lógica para receber o token JWT, validar sua assinatura, verificar a expiração e extrair as permissões (roles).
    *   **Com Gateway:** O gateway intercepta *todas* as requisições. Ele valida o token JWT. Se o token for inválido, ele rejeita a requisição imediatamente com um erro `401 Unauthorized`, e o serviço de destino nunca é acionado. Se for válido, ele encaminha a requisição, possivelmente adicionando cabeçalhos com informações do usuário (como `X-User-Id` ou `X-User-Roles`), que os serviços internos podem consumir com segurança.

3.  **Simula o Ambiente de Produção:** O objetivo de um desafio acadêmico é aprender a construir sistemas como seriam no mundo real. Em qualquer ambiente de produção na nuvem, um API Gateway (ou um Ingress Controller com funcionalidades semelhantes) é uma peça padrão. Implementá-lo localmente garante que a arquitetura esteja correta desde o início.

**Como ele seria implementado em um projeto Spring?**

Você usaria o **Spring Cloud Gateway**. Seria um microserviço Spring Boot separado, cuja principal responsabilidade é rotear o tráfego.

**Dependência Maven:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

**Configuração (`application.yml` do serviço de gateway):**
```yaml
server:
  port: 8080 # O gateway escuta na porta principal

spring:
  cloud:
    gateway:
      routes:
        - id: service-auth # Rota para o serviço de autenticação
          uri: http://localhost:8081 # Endereço do serviço de autenticação
          predicates:
            - Path=/auth/** # Se a URL começar com /auth/, envie para este serviço

        - id: service-agendamento # Rota para o serviço de agendamento
          uri: http://localhost:8082
          predicates:
            - Path=/agendamentos/**

        - id: service-historico # Rota para o serviço de histórico
          uri: http://localhost:8083
          predicates:
            - Path=/historico/**
```
Neste cenário, todos os microserviços individuais (`service-auth`, `service-agendamento`, etc.) rodariam em suas próprias portas, mas o cliente só se comunicaria com a porta `8080`.

---

## 2. Monorepo com Módulos Java (Maven)

**Sim, para um projeto deste escopo, um monorepo com módulos Maven é a abordagem mais prática e recomendada.**

Isso simplifica o gerenciamento do código, o build e o compartilhamento de dependências, sendo ideal para equipes pequenas ou projetos individuais.

**Vantagens do Monorepo para este cenário:**

*   **Build Simplificado:** Você pode compilar e empacotar todos os microserviços com um único comando (`mvn clean install`) a partir do diretório raiz.
*   **Gerenciamento de Dependências Centralizado:** Você pode definir as versões de todas as dependências (como Spring Boot, Spring Cloud, etc.) no `pom.xml` pai, garantindo que todos os módulos usem as mesmas versões, evitando conflitos.
*   **Refatoração Atômica:** Se você precisar alterar um DTO (Data Transfer Object) que é usado por múltiplos serviços, a mudança pode ser feita e commitada de uma só vez, já que está tudo no mesmo repositório.
*   **Facilidade de Navegação:** É mais fácil abrir o projeto inteiro em uma IDE e navegar entre os serviços.

**Estrutura de Pastas (Java Spring com Maven):**

A estrutura seria organizada com um `pom.xml` pai na raiz e cada microserviço sendo um módulo Maven separado em sua própria subpasta.

```
tech-challenge-fase03/
├── .gitignore
├── pom.xml  <-- POM PAI, que gerencia tudo

├── api-gateway/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│               └── application.yml

├── service-auth/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│               └── application.yml

├── service-agendamento/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│               └── application.yml

├── service-notificacoes/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│               └── application.yml

└── service-historico/
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            └── resources/
                └── application.yml
```

**Exemplo do `pom.xml` PAI (raiz):**
Este arquivo define os módulos e gerencia as versões das dependências para todos os submódulos.

```xml
<project ...>
    <groupId>br.com.postech</groupId>
    <artifactId>tech-challenge-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging> <!-- Importante: o packaging é 'pom' -->

    <modules>
        <module>api-gateway</module>
        <module>service-auth</module>
        <module>service-agendamento</module>
        <module>service-notificacoes</module>
        <module>service-historico</module>
    </modules>

    <!-- Gerenciamento de dependências para todos os módulos filhos -->
    <dependencyManagement>
        <dependencies>
            <!-- Ex: Definir a versão do Spring Boot aqui -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>3.2.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

</project>
```

**Exemplo do `pom.xml` de um módulo filho (ex: `service-agendamento/pom.xml`):**
Este arquivo herda do pai e define apenas as dependências específicas deste módulo.

```xml
<project ...>
    <modelVersion>4.0.0</modelVersion>

    <!-- Referência ao projeto pai -->
    <parent>
        <groupId>br.com.postech</groupId>
        <artifactId>tech-challenge-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>service-agendamento</artifactId>
    <name>Serviço de Agendamento</name>

    <dependencies>
        <!-- Não precisa especificar a versão, pois é gerenciada pelo pai -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
    </dependencies>

</project>
```

Essa abordagem combina a organização lógica dos microserviços com a praticidade de gerenciamento de um monorepo, sendo a escolha ideal para o seu desafio.
