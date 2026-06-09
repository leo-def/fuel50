# Fuel50 - Technical Specification

> Reactive microservices platform built with Spring Boot 3 + Spring AI + R2DBC.
> Demonstrates polyglot microservices, shared library patterns, and AI-driven data generation.

## Executive Summary

Fuel50 is a Gradle multi-module Java 21 project containing **6 independent Spring Boot microservices** and **3 shared libraries**. It models a corporate activity management platform (companies → users → activities → actions → ratings) with fully reactive pipelines using Spring WebFlux + R2DBC. A standout feature is the `data-generator-service` which uses **Spring AI + Ollama** to generate realistic seed data from natural language prompts.

---

## 1. Problem Statement

### Context
Demonstrates enterprise-grade Java microservice architecture with reactive programming, shared-library patterns, and AI-assisted tooling.

### Goals
- Model a company activity lifecycle (create company → add users → assign activities → perform actions → rate)
- Show reactive non-blocking I/O end-to-end with R2DBC + WebFlux
- Provide a shared-library pattern for DTOs, domain objects, and Feign-style HTTP clients
- Use generative AI to bootstrap realistic test data

### Success Metrics
- [x] 6 independently deployable Spring Boot services
- [x] Shared library pattern (`common-domains`, `common-dtos`, `common-clients`)
- [x] Reactive end-to-end (R2DBC + WebFlux)
- [x] AI data generation via Spring AI + Ollama streaming
- [x] Docker Compose + Kubernetes manifests
- [ ] Test coverage > 80% per service
- [ ] Service discovery / API gateway

---

## 2. Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 21 |
| Build | Gradle | 8.14.2 |
| Framework | Spring Boot | 3.3.3 |
| Reactive HTTP | Spring WebFlux | 3.3.3 |
| Reactive ORM | Spring Data R2DBC | 3.3.3 |
| AI | Spring AI + Ollama | Latest |
| Mapping | ModelMapper | 3.x |
| Boilerplate | Lombok | 1.18.36 |
| Testing | JUnit 5 | 5.10.0 |
| Containers | Docker + Kubernetes | Latest |

---

## 3. Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                        Shared Libraries                              │
│  common-domains (R2DBC entities) | common-dtos | common-clients      │
└──────────────────────────────────┬───────────────────────────────────┘
                                   │
      ┌─────────┬──────────┬───────┴──────┬──────────┬────────────────┐
      ▼         ▼          ▼              ▼          ▼                ▼
company-   user-      activity-      action-    rating-    data-generator
service   service     service        service    service      service
  :8081    :8082       :8083          :8084      :8085         :8086
      │         │          │              │          │
      └─────────┴──────────┴──────────────┴──────────┘
                              │
                      ┌───────▼───────┐
                      │   MySQL DB    │
                      │  (per schema) │
                      └───────────────┘
```

### Service Responsibilities

| Service | Port | Responsibility |
|---------|------|----------------|
| `company-service` | 8081 | CRUD for Company entities |
| `user-service` | 8082 | CRUD for User entities |
| `activity-service` | 8083 | Activities + UserActivity linking |
| `action-service` | 8084 | Actions + UserAction (performed actions) |
| `rating-service` | 8085 | Rating management |
| `data-generator-service` | 8086 | AI-driven seed data generation via Ollama |

---

## 4. Module Structure

### Shared Libraries (`libs/`)

**`common-domains`** — Spring Data R2DBC annotated entities:
- `Company` — `@Table("company")`: id, name, description, createdBy, createdAt, updatedAt
- `Activity` — `@Table("activity")`: id, companyId, name, description, createdBy, createdAt, updatedAt
- `UserAction` — `@Table("user_action")`: id, userId, actionId, performedAt, metadata (JSON string)
- `Rating`, `UserActivity`, `Action`, `User`

**`common-dtos`** — Lombok DTOs: `UserDto`, `CompanyDto`, `ActivityDto`, `UserActivityDto`, `ActionDto`, `UserActionDto`, `RatingDto`

**`common-clients`** — Reactive HTTP clients: `UserClient`, `CompanyClient`, `ActivityClient`, `ActionClient`, `UserActivityClient`

### Services (`apps/`)

Each service follows the same internal structure:
```
App.java            # Spring Boot entry point
config/
  ModelMapperConfig # ModelMapper bean
controllers/        # REST controllers (+ nested by parent resource)
mappers/            # Domain → DTO mappers
repositories/       # R2DBC reactive repositories
services/           # Business logic (reactive Flux/Mono)
```

### data-generator-service (unique)
```
ai/
  model/            # Plan, PlanResult, CompanySpec, UserSpec, ActivitySpec, ActionSpec
  services/
    PromptAiService # Uses Spring AI ChatClient streaming to generate Plan JSON
factories/          # ActionFactory, ActivityFactory, CompanyFactory, UserFactory, etc.
services/
  PlanSeedService   # Calls factories to seed data into other services via HTTP clients
controllers/
  PromptController  # POST /prompt — accepts user prompt, streams AI response, seeds data
```

**AI Flow:**
```
POST /prompt (userPrompt)
    ↓
PromptAiService.generatePlan(prompt)
    → ChatClient streams Ollama response as Flux<String>
    → reduce to full JSON string
    → deserialize to Plan object
    ↓
PlanSeedService.seed(plan)
    → Create companies via CompanyClient
    → Create users via UserClient
    → Create activities, actions
    ↓
Return PlanResult
```

---

## 5. API Endpoints

Each service exposes standard CRUD + nested routes:

```
# company-service
GET/POST   /company
GET/PUT/DELETE /company/{id}

# user-service
GET/POST   /user
GET/PUT/DELETE /user/{id}

# activity-service
GET/POST   /activity
GET/PUT/DELETE /activity/{id}
GET/POST   /user/{userId}/activity    ← nested UserActivity
GET/POST   /user-activity

# action-service
GET/POST   /action
GET/PUT/DELETE /action/{id}
GET/POST   /activity/{activityId}/action   ← nested by activity
GET/POST   /user-action

# rating-service
GET/POST   /rating
GET/PUT/DELETE /rating/{id}

# data-generator-service
POST /prompt   ← AI data generation endpoint
```

---

## 6. Data Models

Key domain models (Spring Data R2DBC `@Table` annotations):

| Entity | Key Fields |
|--------|-----------|
| Company | id, name, description, createdBy, createdAt |
| User | id, name, email, isAdmin, companyId |
| Activity | id, companyId, name, description, createdBy |
| UserActivity | id, userId, activityId, joinedAt |
| Action | id, activityId, name, description |
| UserAction | id, userId, actionId, performedAt, metadata (JSON) |
| Rating | id, userId, value, createdAt |

---

## 7. Testing Strategy

```bash
./gradlew test              # Run all tests (JUnit 5)
./gradlew :apps:user-service:test
```

- JUnit 5 platform configured globally in root `build.gradle`
- Test coverage: minimal (scaffolded, not fully implemented)

---

## 8. Deployment & Operations

```bash
# Docker Compose
docker-compose up

# Kubernetes
kubectl apply -f k8s/stack.yaml

# Build all
./gradlew build

# Build specific service
./gradlew :apps:user-service:bootJar
```

**Database:** MySQL (schema per service, defined in `database/schema.sql`)

---

## 9. Issues Found

### Critical
- **`UserAction.metadata` is stored as raw String** (`private String metadata`) — no JSON validation or deserialization. Sending malformed JSON will silently persist invalid data.
- **No authentication or authorization** — all endpoints are publicly accessible. Services communicate without mTLS or API keys.
- **`PromptAiService.generatePlan`** parses AI streaming output with `objectMapper.readValue(json, Plan.class)` — if Ollama returns partial/malformed JSON (common with streaming), the whole request fails with a `RuntimeException` wrapping the Jackson exception, with no retry or fallback.

### Logic Issues
- **`PlanSeedService`** calls external services via HTTP clients to seed data, but there is no transactional rollback if a mid-plan step fails (e.g., company created but user creation fails). Partial seed data is left in the system.
- **`UserAction.performedAt`** is a `LocalDateTime` — no timezone info stored. Comparing timestamps across timezones will produce incorrect results.

### Code Quality
- **`metadata` in `UserAction`** is documented as "store JSON as string" but the entire library has no validation utilities. Should use `@Lob` + Jackson `@JsonRawValue` or a proper JSON column type.
- Missing `@SpringBootTest` integration tests for each service.
- `ModelMapper` beans are duplicated in every service's `config/` — should be moved to `common-clients` or a shared `common-config` library.
