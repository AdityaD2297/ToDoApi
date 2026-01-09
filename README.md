# ✅ ToDo API

A secure, production-ready **multi-user ToDo management backend** built with Spring Boot, JWT authentication, and PostgreSQL.

---

## 🚀 Features

- JWT-based authentication (stateless)
- User-specific todos (strict data isolation)
- CRUD operations (Create, Read, Update, Patch, Delete)
- Pagination & sorting
- Advanced filtering (status, priority, completed)
- Case-insensitive title search
- Role-based authorization
- Rate limiting
- Automatic timestamps
- Clean layered architecture

---

## 🧱 Architecture

Controller → Service → Repository → Database
↘ Security Filters ↙

Cross-cutting:
- JWT Authentication
- Rate Limiting
- Validation
- Global Exception Handling

---

## 🛠 Tech Stack

| Layer | Technology |
|-----|-----------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA |
| Database | PostgreSQL |
| Build Tool | Maven |
| Logging | SLF4J |

---

## 🔐 Authentication

- Stateless JWT authentication
- Token contains:
  - userId
  - role
- JWT required for all `/todos/**` endpoints

Authorization: Bearer <jwt-token>

---

## 📦 API Endpoints

### Create ToDo
`POST /todos`

```json
{
  "title": "Finish documentation",
  "description": "Prepare final README",
  "status": "PENDING",
  "priority": "HIGH"
}
Get ToDo by ID
GET /todos/{id}

List Todos (Filters + Pagination)
GET /todos?status=PENDING&completed=false&page=0&size=10

Update ToDo (Full)
PUT /todos/{id}

Patch ToDo (Partial)
PATCH /todos/{id}

Delete ToDo
DELETE /todos/{id}

🧪 Validation Rules
title → required
status → required
priority → required on create
Patch allows partial updates

🗃 Database
PostgreSQL
One-to-many: User → Todos
Enforced via foreign keys

🚦 Rate Limiting
Applied after authentication
Limits per user
Implemented via security filter

