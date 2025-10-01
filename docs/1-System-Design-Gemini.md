# 3 - Setup projeto

## Prompt
> Faça um system design completo a partir da descrição de trabalho anexada no pdf. Comece entendendo o problema e fazendo perguntas de clarificação quando necessario para estabelecer o escopo. Faça o design das APIs, especificando endpoints e modelos resumidos de contratos. Faça um desenho de alto nivel da arquitetura, detalhando o fluxo de comunicação entre todos microservices. Complete detalhando soluções para os problemas encontrados, como banco de dados utilizado, autenticação utilizada, funcionamento do graphql vs rest, etc. O projeto será implementado em java spring, sinta-se livre para definir o resto.

## Resposta
Apresento um design de sistema completo para a aplicação hospitalar.

## 1. Entendimento do Problema e Escopo

O objetivo é projetar o backend para um sistema de gerenciamento hospitalar. O sistema deve permitir o agendamento de consultas, o gerenciamento de históricos de pacientes e o envio de notificações de lembrete. A arquitetura deve ser baseada em microserviços, com foco em segurança, escalabilidade e comunicação assíncrona, utilizando Java e o ecossistema Spring.

**Atores Principais:**
*   **Médicos:** Podem visualizar e editar o histórico de consultas.
*   **Enfermeiros:** Podem registrar novas consultas, modificar as existentes e acessar o histórico.
*   **Pacientes:** Podem visualizar apenas suas próprias consultas.

## 2. Perguntas de Clarificação e Suposições

Para um projeto real, faríamos as seguintes perguntas para refinar o escopo. Para este exercício, farei algumas suposições.

*   **Qual a carga de usuários esperada?**
    *   **Suposição:** O sistema é para um hospital de médio porte. O design deve ser escalável, mas não precisa ser otimizado para milhões de usuários concorrentes inicialmente.

*   **O termo "autenticação básica" deve ser levado ao pé da letra?**
    *   **Suposição:** "Autenticação básica" refere-se ao requisito fundamental de ter autenticação. Para uma arquitetura de microserviços, um sistema baseado em tokens (como JWT) é mais seguro e escalável do que o Basic Auth do HTTP. Portanto, o design usará JWT.

*   **Qual a preferência entre RabbitMQ e Kafka?**
    *   **Suposição:** Para o caso de uso de notificar sobre a criação/edição de consultas, RabbitMQ é uma escolha excelente e mais simples de configurar do que Kafka. O design utilizará RabbitMQ.

*   **Quais os requisitos de retenção de dados para o histórico do paciente?**
    *   **Suposição:** Os dados do histórico do paciente devem ser mantidos por um longo período por razões médicas e legais. A escolha do banco de dados deve considerar isso.

## 3. Design da Arquitetura de Alto Nível

A arquitetura será composta por múltiplos microserviços, cada um com uma responsabilidade única, comunicando-se de forma síncrona via APIs REST/GraphQL e de forma assíncrona através de um message broker (RabbitMQ). Um API Gateway servirá como ponto de entrada único para todas as requisições dos clientes.

**Microserviços:**
1.  **Serviço de Autenticação e Usuários:** Gerencia os dados dos usuários (médicos, enfermeiros, pacientes), suas permissões (roles) e lida com o processo de login, emitindo tokens JWT.
2.  **Serviço de Agendamento:** Responsável por toda a lógica de negócio de criação, alteração e consulta de agendamentos.
3.  **Serviço de Notificações:** Ouve eventos de agendamento e envia lembretes aos pacientes (por e-mail, SMS, etc.).
4.  **Serviço de Histórico:** Armazena uma cópia dos dados de consulta para fins de histórico e expõe esses dados através de uma API GraphQL para consultas flexíveis.
5.  **API Gateway:** Ponto de entrada único que roteia as requisições para os serviços apropriados. Também é responsável por validar os tokens JWT.

## 4. Design das APIs

### Serviço de Autenticação (REST)
*   **`POST /auth/login`**: Autentica um usuário.
    *   **Request Body**: `{ "email": "user@example.com", "password": "password123" }`
    *   **Response Body**: `{ "token": "jwt.token.string" }`

### Serviço de Agendamento (REST)
*   **`POST /agendamentos`**: Cria uma nova consulta (acessível por Enfermeiros).
    *   **Request Body**: `{ "pacienteId": 123, "medicoId": 456, "dataHora": "2025-12-20T10:00:00Z", "motivo": "Check-up" }`
    *   **Response Body**: `{ "id": 1, "pacienteId": 123, "medicoId": 456, "dataHora": "2025-12-20T10:00:00Z", "status": "AGENDADO" }`

*   **`PUT /agendamentos/{id}`**: Modifica uma consulta existente (acessível por Médicos e Enfermeiros).
    *   **Request Body**: `{ "dataHora": "2025-12-21T11:00:00Z", "status": "REMARCADO" }`
    *   **Response Body**: `{ "id": 1, ... }`

*   **`GET /agendamentos/paciente/{id}`**: Lista as consultas de um paciente (acessível por Pacientes para seu próprio ID, Médicos e Enfermeiros para qualquer ID).
    *   **Response Body**: `[ { "id": 1, ... }, { "id": 2, ... } ]`

### Serviço de Histórico (GraphQL)
*   **Endpoint**: `POST /historico/graphql`
*   **Exemplo de Query**: Listar todos os atendimentos de um paciente.
    ```graphql
    query {
      historicoPaciente(pacienteId: "123") {
        id
        dataHora
        medico {
          nome
          especialidade
        }
        notas
      }
    }
    ```
*   **Exemplo de Query**: Listar apenas as consultas futuras de um paciente.
    ```graphql
    query {
      consultasFuturas(pacienteId: "123") {
        id
        dataHora
        status
      }
    }
    ```

### 5. Detalhamento das Soluções

### Escolha do Banco de Dados
*   **Serviço de Autenticação e Agendamento (PostgreSQL):** Utilizaremos um banco de dados relacional como o PostgreSQL. Os dados de usuários e agendamentos são estruturados e se beneficiam da consistência e das transações ACID (Atomicidade, Consistência, Isolamento e Durabilidade), que são cruciais para operações como agendar uma consulta.

*   **Serviço de Histórico (MongoDB - Opcional):** Para o serviço de histórico, um banco de dados NoSQL como o MongoDB é uma ótima opção. Ele armazena dados em um formato semelhante a JSON, o que o torna muito flexível para armazenar "snapshots" das consultas. Se a estrutura de uma consulta mudar no futuro, o serviço de histórico pode lidar com diferentes versões dos dados sem problemas.

### Autenticação e Autorização com Spring Security e JWT
1.  **Fluxo de Autenticação:**
    *   O cliente envia credenciais (email/senha) para o `Serviço de Autenticação`.
    *   O serviço valida as credenciais e, se corretas, gera um JWT contendo o `userId` e as `roles` (ex: `ROLE_MEDICO`, `ROLE_PACIENTE`).
    *   O token é retornado ao cliente.

2.  **Fluxo de Autorização:**
    *   Para cada requisição subsequente, o cliente envia o JWT no cabeçalho `Authorization: Bearer <token>`.
    *   O **API Gateway** intercepta a requisição, valida a assinatura do token e, se válido, encaminha a requisição para o serviço de destino.
    *   Dentro de cada microserviço, o **Spring Security** é configurado para ler as `roles` do token e proteger os endpoints. Por exemplo:
        ```java
        @PreAuthorize("hasRole('ENFERMEIRO') or hasRole('MEDICO')")
        @PutMapping("/agendamentos/{id}")
        public ResponseEntity<Consulta> modificarConsulta(...) { ... }

        @PreAuthorize("#id == authentication.principal.id or hasRole('MEDICO')")
        @GetMapping("/agendamentos/paciente/{id}")
        public ResponseEntity<List<Consulta>> verConsultas(...) { ... }
        ```

#### Comunicação Assíncrona com RabbitMQ
Este fluxo garante que os serviços sejam desacoplados e resilientes.

1.  Um enfermeiro cria uma nova consulta através de uma chamada ao **Serviço de Agendamento**.
2.  O **Serviço de Agendamento** salva a consulta em seu banco de dados (PostgreSQL).
3.  Imediatamente após salvar, ele publica uma mensagem em um tópico do RabbitMQ chamado `agendamento.exchange`. A mensagem contém os detalhes da consulta, como `consultaId`, `pacienteId`, `dataHora`.
    *   **Routing Key:** `consulta.criada`
4.  O **Serviço de Notificações** e o **Serviço de Histórico** possuem filas inscritas nesse tópico.
5.  O **Serviço de Notificações** recebe a mensagem, processa-a (ex: agenda o envio de um lembrete 24 horas antes da consulta) e envia um e-mail de confirmação ao paciente.
6.  O **Serviço de Histórico** recebe a mesma mensagem e salva uma cópia dos dados da consulta em seu banco de dados (MongoDB) para ser disponibilizada via GraphQL.

### GraphQL vs. REST
*   **REST** é utilizado no **Serviço de Agendamento** para operações de "comando" (criar, editar), que são ações diretas e bem definidas. Os endpoints são simples e seguem um padrão claro.
*   **GraphQL** é utilizado no **Serviço de Histórico** porque ele brilha em cenários de "query" complexas. O frontend pode precisar de diferentes combinações de dados do histórico do paciente. Com GraphQL, o cliente pode solicitar exatamente os campos de que precisa em uma única requisição, evitando tanto a busca excessiva de dados (`over-fetching`) quanto a necessidade de fazer múltiplas chamadas (`under-fetching`).

Este design de sistema atende a todos os requisitos do desafio, estabelecendo uma base robusta, segura e escalável para a aplicação hospitalar.