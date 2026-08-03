# Generic JWT Authentication API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-brightgreen.svg)](https://spring.io/projects/spring-boot)

Uma API RESTful genérica de autenticação, criada para treinar e aplicar conceitos estudados, desenvolvida com **Java 21** e **Spring Boot 3**.

Este projeto foi construído com foco na aplicação de padrões profissionais de arquitetura de software, incluindo **Design Patterns (DTOs, Builder)**, **Tratamento Global de Exceções (`@ControllerAdvice`)**, validação de dados e separação clara de responsabilidades seguindo princípios de **Clean Architecture**.

---

#  Funcionalidades

- ✅ CRUD completo de clientes.
- ✅ Validação de dados utilizando Bean Validation.
- ✅ CPF único e e-mail válido.
- ✅ Tratamento global de exceções seguindo o padrão RFC 7807.
- ✅ Arquitetura em camadas.
- ✅ DTOs para comunicação entre API e cliente.
- ✅ Paginação e ordenação de resultados

---

#  Como Executar

## Pré-requisitos

Antes de iniciar, certifique-se de possuir instalado:

- Java 21+
- Maven 3.8+
- MySQL 8+

---

## 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/generic-commerce-api.git
cd generic-commerce-api
```

---

## 2. Crie o banco de dados

```sql
CREATE DATABASE commerce_db;
```

---

## 3. Variáveis de ambiente

| Variável | Descrição |
|----------|-----------|
| DB_URL | URL de conexão com o banco |
| DB_USERNAME | Usuário do banco |
| DB_PASSWORD | Senha do banco |

---

## 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

#  Exemplo de Uso

## Requisição

Cadastrar um cliente.

```bash
curl -X POST http://localhost:8080/api/v1/clientes \
-H "Content-Type: application/json" \
-d '{
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "cpf": "12345678900",
}'
```

---

## Resposta

**201 Created**

```json
{
  "id": 1,
  "nome": "João da Silva",
  "cpf": "12345678900",
  "email": "joao.silva@email.com",
  "pontoFidelidade": 0
}
```

---

#  Referência da API

Todas as rotas possuem o prefixo:

```
/api/v1
```

| Método | Endpoint | Descrição | Resposta |
|---------|----------|-----------|-----------|
| POST | `/clientes` | Cadastra um novo cliente | **201 Created** |
| GET | `/clientes` | Lista clientes com paginação | **200 OK** |
| GET | `/clientes/{id}` | Busca cliente pelo ID | **200 OK** |
| PUT | `/clientes/{id}` | Atualiza um cliente | **200 OK** |
| DELETE | `/clientes/{id}` | Remove um cliente | **204 No Content** |

---

---

# Arquitetura

O projeto segue uma arquitetura em camadas visando organização, escalabilidade e facilidade de manutenção.

```
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Database
```

## Camadas

### Controllers

Responsáveis por receber as requisições HTTP e retornar respostas padronizadas.

### Services

Contêm toda a lógica de negócio da aplicação.

### Repositories

Camada de persistência utilizando Spring Data JPA.

### DTOs

Responsáveis por transportar dados entre cliente e servidor, evitando expor diretamente as entidades do banco de dados.

---

# ⚙️ Stack Tecnológica

| Tecnologia | Utilização |
|------------|------------|
| Java 21 | Linguagem principal |
| Spring Boot 3 | Framework principal |
| Spring Data JPA | Persistência |
| Hibernate | ORM |
| MySQL | Banco de dados |
| Bean Validation | Validação |
| Lombok | Redução de código boilerplate ||
| JUnit 5 | Testes |
| Mockito | Testes unitários |

---

#  Testes

Execute todos os testes utilizando:

```bash
./mvnw test
```

São executados:

- Testes unitários
- Validação das regras de negócio

---

# Estrutura do Projeto

```text
src
├── main
│   ├── java
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── dto
│   │   ├── model
│   │   ├── exception
│   │   └── config
│   └── resources
│       ├── application.properties
│       └── ...
└── test
```
