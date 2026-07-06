# 💸 FinTrack API

> API REST para gestão financeira pessoal — controle de contas, receitas, despesas e transferências com segurança e arquitetura profissional.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat-square&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-black?style=flat-square&logo=jsonwebtokens)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red?style=flat-square)
![JUnit](https://img.shields.io/badge/JUnit_5-Testes-brightgreen?style=flat-square)

---

## 📋 Sobre o Projeto

O **FinTrack API** é um sistema backend completo de gestão financeira pessoal desenvolvido com Java e Spring Boot. O projeto foi construído seguindo boas práticas de mercado — arquitetura em camadas, segurança com JWT, validações, tratamento global de erros, migrations versionadas e testes automatizados.

### Funcionalidades

- ✅ Cadastro e autenticação de usuários com JWT
- ✅ Gerenciamento de contas bancárias (Corrente, Poupança, Investimento)
- ✅ Registro de receitas e despesas com atualização automática de saldo
- ✅ Transferências entre contas com lançamento duplo
- ✅ Cancelamento de transações com reversão de saldo
- ✅ Relatório financeiro por período
- ✅ Isolamento total de dados por usuário autenticado
- ✅ Categorias do sistema para classificação de transações

---

## 🏗️ Arquitetura

```
src/main/java/com/project/fintrackApi/
│
├── controller/         # Endpoints REST — recebe e responde requisições HTTP
├── service/            # Regras de negócio
├── repository/         # Acesso ao banco de dados via JPA
├── domain/             # Entidades e enums
│   └── enums/
├── dto/
│   ├── request/        # Dados que entram na API
│   └── response/       # Dados que saem da API
├── config/
│   └── security/       # Spring Security + JWT Filter
└── exception/          # Tratamento global de erros
```

---

## 🔒 Segurança

A autenticação é **stateless** via JWT. Cada requisição protegida deve conter o token no header:

```
Authorization: Bearer {token}
```

- Senhas armazenadas com hash **BCrypt**
- Token com expiração de **24 horas**
- Cada usuário acessa **somente seus próprios dados**
- Proteção contra **IDOR** — recursos de outros usuários retornam 404

---

## 🗄️ Modelo de Dados

```
Usuario (1) ──── (N) Conta (1) ──── (N) Transacao
                                         │
                                    Categoria (N:1)
```

### Tipos de Transação

| Tipo | Comportamento |
|------|--------------|
| `RECEITA` | Aumenta o saldo da conta |
| `DESPESA` | Diminui o saldo — rejeita se saldo insuficiente |
| `TRANSFERENCIA` | Débito na origem e crédito no destino — atomicamente |

---

## 🚀 Endpoints

### 🔓 Públicos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/usuarios` | Cadastrar usuário |
| `POST` | `/api/auth/login` | Login — retorna token JWT |

### 🔐 Autenticados

#### Contas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/contas` | Criar conta |
| `GET` | `/api/contas` | Listar contas do usuário |
| `GET` | `/api/contas/{id}` | Buscar conta por ID |
| `DELETE` | `/api/contas/{id}` | Deletar conta |

#### Transações
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/transacoes` | Criar transação |
| `GET` | `/api/transacoes` | Listar todas as transações |
| `GET` | `/api/transacoes/conta/{id}` | Listar por conta |
| `GET` | `/api/transacoes/resumo/{id}?dataInicio=&dataFim=` | Relatório por período |
| `DELETE` | `/api/transacoes/{id}` | Cancelar transação |

---

## 📦 Exemplos de Uso

### Cadastro de Usuário
```http
POST /api/usuarios
Content-Type: application/json

{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "123456"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "joao@email.com",
    "senha": "123456"
}
```
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tipo": "Bearer",
    "nome": "João Silva",
    "email": "joao@email.com"
}
```

### Criar Transação
```http
POST /api/transacoes
Authorization: Bearer {token}
Content-Type: application/json

{
    "valor": 1500.00,
    "tipoTransacao": "RECEITA",
    "descricao": "Salário",
    "contaOrigemId": 1
}
```

### Relatório por Período
```http
GET /api/transacoes/resumo/1?dataInicio=2024-01-01&dataFim=2024-01-31
Authorization: Bearer {token}
```
```json
{
    "totalReceitas": 5000.00,
    "totalDespesas": 1500.00,
    "saldoPeriodo": 3500.00,
    "quantidadeTransacoes": 8
}
```

---

## ⚠️ Tratamento de Erros

Todas as respostas de erro seguem o padrão:

```json
{
    "status": 404,
    "erro": "Recurso não encontrado",
    "mensagem": "Conta não encontrada",
    "path": "/api/contas/99",
    "timestamp": "2024-01-15T10:30:00"
}
```

| Status | Situação |
|--------|----------|
| `400` | Dados inválidos — campos obrigatórios ou formato incorreto |
| `401` | Token ausente ou inválido |
| `403` | Sem permissão de acesso |
| `404` | Recurso não encontrado ou não pertence ao usuário |
| `409` | Conflito — ex: email já cadastrado |
| `422` | Saldo insuficiente para a operação |
| `500` | Erro interno inesperado |

---

## 🧪 Testes

O projeto conta com testes unitários cobrindo as regras de negócio críticas:

```bash
./mvnw test
```

**Cenários testados:**
- ✅ Receita aumenta o saldo corretamente
- ✅ Despesa diminui o saldo corretamente
- ✅ Saldo insuficiente lança exception sem alterar dados
- ✅ Conta não encontrada lança exception
- ✅ Transferência movimenta dois saldos simultaneamente
- ✅ Transferência para mesma conta é rejeitada
- ✅ Cancelamento de despesa reverte saldo
- ✅ Cancelamento de receita reverte saldo

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.3 | Framework base |
| Spring Security | 6 | Autenticação e autorização |
| Spring Data JPA | 3.2.3 | Persistência de dados |
| Hibernate | 6.4 | ORM |
| PostgreSQL | 17 | Banco de dados |
| Flyway | 9.22 | Versionamento do banco |
| JWT (Auth0) | 4.4 | Geração e validação de tokens |
| BCrypt | - | Hash de senhas |
| Lombok | - | Redução de boilerplate |
| JUnit 5 | - | Testes unitários |
| Mockito | - | Mock de dependências |
| Maven | 3 | Gerenciamento de dependências |

---

## ⚙️ Como Executar Localmente

### Pré-requisitos

- Java 17+
- Maven 3+
- PostgreSQL 15+

### Passo a Passo

**1. Clone o repositório**
```bash
git clone https://github.com/MayconMsilva/fintrack-api.git
cd fintrack-api
```

**2. Crie o banco de dados**
```sql
CREATE DATABASE fintrack;
```

**3. Configure as variáveis de ambiente**
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/fintrack
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha
JWT_SECRET=fintrack-secret-key-dev
```

Ou edite diretamente o `application.properties`.

**4. Execute a aplicação**
```bash
./mvnw spring-boot:run
```

O Flyway criará as tabelas automaticamente na primeira execução.

**5. Acesse a API**
```
http://localhost:8080
```

---

## 📁 Migrations

O banco é versionado com Flyway:

```
V1 — Tabela de usuários
V2 — Tabela de categorias
V3 — Tabela de contas
V4 — Tabela de transações
V5 — Seed de categorias do sistema
```

---

## 👤 Autor

**Maycon Mikael**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Maycon_Mikael-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/maycon-mikael-a13311376)
[![GitHub](https://img.shields.io/badge/GitHub-MayconMsilva-black?style=flat-square&logo=github)](https://github.com/MayconMsilva)

---

## 📄 Licença

Este projeto está sob a licença MIT.