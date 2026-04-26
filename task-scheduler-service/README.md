# Task Scheduler Service

Java Spring Boot Gradle MongoDB JWT

Microservico responsavel por cadastro, consulta e atualizacao de tarefas agendadas para notificacao.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Database-47A248)](https://www.mongodb.com/)
[![Gradle](https://img.shields.io/badge/Gradle-Build%20Tool-02303A)](https://gradle.org/)

---

## Sumario

* [Visao Geral](#visao-geral)
* [Papel no Ecossistema](#papel-no-ecossistema)
* [Arquitetura (Visao de Sistema)](#arquitetura-visao-de-sistema)
* [Tecnologias](#tecnologias)
* [Decisoes Tecnicas](#decisoes-tecnicas)
* [Como Executar Localmente](#como-executar-localmente)
* [Configuracao](#configuracao)
* [Autenticacao e Seguranca](#autenticacao-e-seguranca)
* [API](#api)
* [Tratamento de Erros](#tratamento-de-erros)
* [Contratos (DTOs)](#contratos-dtos)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Testes](#testes)

---

## Visao Geral

O `task-scheduler-service` centraliza o ciclo de vida das tarefas de notificacao de um usuario autenticado.

### Responsabilidades

* Criar tarefa para o usuario autenticado
* Listar tarefas do usuario autenticado
* Buscar tarefas pendentes por periodo (agenda de eventos)
* Atualizar dados da tarefa
* Alterar status da tarefa (`PENDENTE`, `NOTIFICADO`, `CANCELADO`)
* Remover tarefa por id

---

## Papel no Ecossistema

Este servico nao atua isoladamente no sistema. Ele participa do fluxo de agenda/notificacao entre outros microservicos.

### Quem consome

* BFF/API Gateway (ou cliente backend que orquestra os fluxos)
* Jobs internos de processamento de eventos agendados

### Dependencias e integracoes

* Depende do `user-service` para validacao/consulta de usuario autenticado
* Serve como fonte de tarefas para o `notification-service` processar notificacoes

### Responsabilidade no sistema

* Manter o estado das tarefas de agenda do usuario
* Expor tarefas pendentes por periodo para processamento assincrono
* Garantir rastreabilidade por status ao longo do ciclo da tarefa

---

## Arquitetura (Visao de Sistema)

Fluxo simplificado:

1. Cliente/BFF envia requisicao com Bearer Token.
2. `task-scheduler-service` valida token e carrega usuario via `user-service`.
3. Tarefa e persistida no MongoDB.
4. Consultas por periodo alimentam processamento de notificacao.
5. `notification-service` (ou job consumidor) atualiza status conforme envio.

---

## Tecnologias

* Java 17
* Spring Boot
* Spring Web
* Spring Data MongoDB
* Spring Security
* JWT
* OpenFeign
* MapStruct
* Gradle

---

## Decisoes Tecnicas

* **MongoDB**: modelo flexivel para evolucao do payload de tarefa sem alto acoplamento de schema relacional.
* **JWT stateless**: autenticacao sem sessao em servidor, adequada para arquitetura de microservicos.
* **OpenFeign**: comunicacao declarativa com o `user-service`, reduzindo boilerplate HTTP.
* **MapStruct**: mapeamento entre DTOs e entidades com menor codigo manual e melhor manutencao.
* **Seguranca sem exposicao de senha**: o `task-scheduler-service` identifica o usuario pelo token JWT; no contexto de `UserDetails`, a senha e tecnica (`{noop}not-used`) e nao depende de senha real no payload do `user-service`.

---

## Como Executar Localmente

### Pre-requisitos

* Java 17+
* MongoDB local
* Git
* Gradle (ou wrapper)
* `user-service` em execucao (para validacao de usuario/token)

### 1) Clonar repositorio

```bash
git clone <url-do-repo>
cd task-scheduler-service
```

### 2) Configurar banco e servicos

Garanta os servicos locais:

* MongoDB em `localhost:27017`
* `user-service` em `localhost:8080`

### 3) Subir aplicacao

```bash
./gradlew bootRun
```

---

## Configuracao

`src/main/resources/application.properties`:

* `spring.data.mongodb.uri=mongodb://localhost:27017/db_agendador`
* `usuario.url=http://localhost:8080`
* `server.port=8081`

### Observacoes para producao

* Externalizar e rotacionar segredo JWT.
* Configurar logs e monitoramento.
* Aplicar estrategia de validacao e autorizacao por recurso (owner check por tarefa).

---

## Autenticacao e Seguranca

* Autenticacao via Bearer Token (JWT).
* Sessao stateless.
* Todos os endpoints exigem autenticacao.
* O servico consulta o `user-service` para carregar dados do usuario no fluxo de autenticacao.

### Exemplo com token

```http
GET /tarefas HTTP/1.1
Host: localhost:8081
Authorization: Bearer <token_jwt>
```

---

## API

Base path: `/tarefas`

### Tarefas

#### `POST /tarefas`

Cria uma nova tarefa para o usuario autenticado.

Exemplo de request:

```json
{
  "nomeTarefa": "Pagar internet",
  "descricao": "Pagamento mensal do provedor",
  "dataEvento": "2026-05-10T09:00:00Z"
}
```

#### `GET /tarefas`

Lista tarefas do usuario autenticado (obtido via token).

#### `GET /tarefas/eventos?dataInicial={instant}&dataFinal={instant}`

Lista tarefas pendentes no periodo informado.

#### `PUT /tarefas?id={id}`

Atualiza os dados da tarefa por id.

#### `PATCH /tarefas?status={status}&id={id}`

Atualiza status da tarefa por id.

#### `DELETE /tarefas?id={id}`

Remove tarefa por id.

---

## Tratamento de Erros

Erros comuns esperados:

* `400 Bad Request`: payload invalido ou parametros ausentes/incorretos.
* `401 Unauthorized`: token ausente, invalido ou expirado.
* `404 Not Found`: tarefa nao encontrada.
* `500 Internal Server Error`: falha interna inesperada.

Exemplo de payload de erro:

```json
{
  "status": 404,
  "message": "Tarefa não encontrada"
}
```

> Observacao: atualmente o projeto retorna mensagens simples em algumas excecoes; o payload acima representa o padrao recomendado.

---

## Contratos (DTOs)

### TarefaRequestDTO

```json
{
  "nomeTarefa": "Pagar internet",
  "descricao": "Pagamento mensal do provedor",
  "dataEvento": "2026-05-10T09:00:00Z"
}
```

### TarefaResponseDTO

```json
{
  "id": "680d26f0b2f4f46f8a9f91bc",
  "nomeTarefa": "Pagar internet",
  "descricao": "Pagamento mensal do provedor",
  "dataCriacao": "2026-04-26T20:35:10Z",
  "dataEvento": "2026-05-10T09:00:00Z",
  "emailUsuario": "user@email.com",
  "dataAlteracao": null,
  "status": "PENDENTE"
}
```

> O request recebe apenas dados de entrada da tarefa; o response contem metadados gerados pelo sistema.
> Datas seguem padrao `Instant` ISO-8601 em UTC (ex.: `2026-05-10T09:00:00Z`).

---

## Estrutura do Projeto

```text
src/main/java/com/azevedo/task_scheduler_service
├── business
│   ├── dto
│   ├── mapper
│   └── service
├── controller
└── infrastructure
    ├── client
    ├── entity
    ├── enums
    ├── exceptions
    ├── repository
    └── security
```

---

## Testes

```bash
./gradlew test
```
