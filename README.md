# 💸 FinTrack — Sistema de Gestão Financeira Pessoal

> Aplicação fullstack para controle de finanças pessoais — gerenciamento de contas, receitas, despesas, transferências e relatórios financeiros.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat-square&logo=postgresql)
![React](https://img.shields.io/badge/React-18-61dafb?style=flat-square&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?style=flat-square&logo=typescript)
![JWT](https://img.shields.io/badge/JWT-Auth-black?style=flat-square&logo=jsonwebtokens)

---

## 📋 Sobre o Projeto

O **FinTrack** é um sistema fullstack de gestão financeira pessoal desenvolvido com **Java + Spring Boot** no backend e **React + TypeScript** no frontend. O projeto foi construído seguindo boas práticas de mercado — arquitetura em camadas, autenticação stateless com JWT, validações, tratamento global de erros, migrations versionadas com Flyway e testes automatizados.

---

## 🖥️ Funcionalidades

- ✅ Cadastro e login de usuários com JWT
- ✅ Dashboard com saldo total de todas as contas
- ✅ Gerenciamento de contas bancárias (Corrente, Poupança, Investimento)
- ✅ Registro de receitas, despesas e transferências
- ✅ Cancelamento de transações com reversão automática de saldo
- ✅ Relatório financeiro por período
- ✅ Isolamento total de dados por usuário autenticado

---

## 🏗️ Arquitetura

```
fintrack/
├── fintrack-api/          # Backend — Java + Spring Boot
│   └── src/main/java/
│       ├── controller/    # Endpoints REST
│       ├── service/       # Regras de negócio
│       ├── repository/    # Acesso ao banco
│       ├── domain/        # Entidades e enums
│       ├── dto/           # Request e Response DTOs
│       ├── config/        # Security, CORS, Flyway
│       └── exception/     # Tratamento global de erros
│
└── fintrack-web/          # Frontend — React + TypeScript
    └── src/
        ├── pages/         # Login, Cadastro, Dashboard, Contas, Transações, Relatório
        ├── components/    # Navbar, PrivateRoute
        ├── services/      # authService, contaService, transacaoService
        └── context/       # AuthContext
```

---

## 🔒 Segurança

- Autenticação **stateless** via **JWT**
- Senhas com hash **BCrypt**
- Token com expiração de **24 horas**
- Cada usuário acessa **somente seus próprios dados**
- Proteção contra **IDOR** — recursos de outros usuários retornam 404
- Interceptors no Axios — token automático em toda requisição
- Redirecionamento automático para login quando token expira

---

## 🗄️ Modelo de Dados

```
Usuario (1) ──── (N) Conta (1) ──── (N) Transacao
                                         │
                                    Categoria (N:1)
```

| Tipo | Comportamento |
|------|--------------|
| `RECEITA` | Aumenta o saldo da conta |
| `DESPESA` | Diminui o saldo — rejeita se insuficiente |
| `TRANSFERENCIA` | Débito na origem e crédito no destino — atomicamente |

---

## 🚀 Endpoints da API

### Públicos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/usuarios` | Cadastrar usuário |
| `POST` | `/api/auth/login` | Login |

### Autenticados — Contas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/contas` | Criar conta |
| `GET` | `/api/contas` | Listar contas |
| `GET` | `/api/contas/{id}` | Buscar por ID |
| `DELETE` | `/api/contas/{id}` | Deletar conta |

### Autenticados — Transações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/transacoes` | Criar transação |
| `GET` | `/api/transacoes` | Listar todas |
| `GET` | `/api/transacoes/conta/{id}` | Listar por conta |
| `GET` | `/api/transacoes/resumo/{id}?dataInicio=&dataFim=` | Relatório |
| `DELETE` | `/api/transacoes/{id}` | Cancelar |

---

## ⚠️ Padrão de Erros

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
| `400` | Dados inválidos |
| `401` | Token inválido |
| `403` | Sem permissão |
| `404` | Não encontrado |
| `409` | Conflito |
| `422` | Saldo insuficiente |
| `500` | Erro interno |

---

## 🧪 Testes Automatizados

```bash
./mvnw test
```

8 testes cobrindo: receita, despesa, saldo insuficiente, transferência e cancelamento.

---

## 🛠️ Tecnologias

### Backend

| Tecnologia | Uso |
|------------|-----|
| Java 17 | Linguagem principal |
| Spring Boot 3.2.3 | Framework base |
| Spring Security 6 | Autenticação |
| Spring Data JPA | Persistência |
| PostgreSQL 17 | Banco de dados |
| Flyway | Migrations |
| JWT Auth0 4.4 | Tokens |
| BCrypt | Hash de senhas |
| JUnit 5 + Mockito | Testes |
| Maven | Dependências |

### Frontend

| Tecnologia | Uso |
|------------|-----|
| React 18 | Framework de UI |
| TypeScript 5 | Tipagem |
| Vite 5 | Build tool |
| React Router DOM 6 | Navegação |
| Axios | Requisições HTTP |
| Context API | Estado global |

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 17+, Maven 3+, PostgreSQL 15+
- Node.js 18+, npm 9+

### Backend

```bash
git clone https://github.com/MayconMsilva/fintrack-api.git
cd fintrack-api
```

```sql
CREATE DATABASE fintrack;
```

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/fintrack}
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:sua_senha}
api.security.token.secret=${JWT_SECRET:fintrack-secret-key-dev}
```

```bash
./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`

### Frontend

```bash
cd fintrack-web
npm install
npm run dev
```

Frontend disponível em `http://localhost:5173`

> ⚠️ Certifique-se de que a API está rodando antes de acessar o frontend.

---

## 📁 Migrations

```
V1 — Usuários
V2 — Categorias
V3 — Contas
V4 — Transações
V5 — Seed categorias do sistema
```

---

## 📁 Páginas do Frontend

```
/login        → Login
/cadastro     → Cadastro
/dashboard    → Saldo total e contas
/contas       → Gerenciamento de contas
/transacoes   → Histórico e registro
/relatorio    → Relatório por período
```

---

## 👤 Autor

**Maycon Mikael**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Maycon_Mikael-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/maycon-mikael-a13311376)
[![GitHub](https://img.shields.io/badge/GitHub-MayconMsilva-black?style=flat-square&logo=github)](https://github.com/MayconMsilva)

---

