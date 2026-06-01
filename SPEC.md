# Fuel50 - Technical Specification

> Technical specification for the Fuel50 Reactive Microservices platform.
> Reference for understanding the polyglot architecture and reactive patterns.

## Executive Summary

- **Project**: Fuel50
- **Type**: Microservices Platform (6 services + 3 shared libraries)
- **Language**: Java 21
- **Framework**: Spring Boot 3.3+ (WebFlux - reactive)
- **Database**: MySQL with R2DBC (async driver)
- **Status**: Active Development
- **Owner**: Development team

---

## 1. Problem Statement

### Context
Fuel50 is a reactive microservices platform demonstrating modern Java best practices with non-blocking I/O, event-driven communication, and polyglot architecture across independent services.

### Goals
- **Primary**: Implement scalable microservices with reactive programming
- **Secondary**: Demonstrate Spring Boot WebFlux and R2DBC best practices
- **Tertiary**: Show service-to-service communication patterns and shared library usage

### Success Metrics
- [x] 6 independent microservices deployed
- [x] Non-blocking I/O (Mono/Flux) throughout
- [x] Shared DTOs and client libraries for inter-service communication
- [x] R2DBC async database access
- [x] Service discovery and resilience patterns
- [ ] <100ms p99 latency per service
- [ ] Horizontal scalability (stateless services)

---

## 2. Technology Stack

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Language | Java | 21 | Latest LTS with virtual threads |
| Framework | Spring Boot | 3.3+ | Full reactive support (WebFlux) |
| Reactive | Project Reactor | Latest | Mono/Flux for async operations |
| Database | MySQL | 8.0+ | Relational DB with strong consistency |
| Driver | R2DBC | Latest | Async, non-blocking database access |
| Build | Gradle | 8.x | Build automation and dependency management |
| Testing | JUnit 5 + Testcontainers | Latest | Modern testing with Docker containers |
| API Docs | Springdoc | Latest | OpenAPI/Swagger generation |
| Observability | Micrometer | Latest | Metrics and tracing |

### Services & Libraries

**Microservices** (apps/):
- `user-service`: User management and authentication
- `company-service`: Company/organization management
- `action-service`: Actions and workflow management
- `activity-service`: Activity logging and analytics
- `rating-service`: Rating and review system
- `data-generator-service`: Test data generation

**Shared Libraries** (libs/):
- `common-dtos`: DTOs shared across services
- `common-domains`: Domain models
- `common-clients`: HTTP clients for inter-service communication

---

## 3. Architecture

### Microservices Architecture

```
┌─────────────────────────────────────────────────────────┐
│               API Gateway / Load Balancer               │
└────────────────────┬────────────────────────────────────┘
                     │
    ┌────────────────┼────────────────┐
    │                │                │
┌───▼────┐      ┌───▼────┐      ┌────▼───┐
│ User   │      │Company │      │ Action │
│Service │      │Service │      │Service │
└───┬────┘      └───┬────┘      └────┬───┘
    │               │               │
    │   ┌───────────────────────────┤
    │   │                           │
┌───▼───────────────────────────────▼───┐
│         MySQL Database (R2DBC)        │
│  (Tables for each service domain)     │
└─────────────────────────────────────────┘
    │               │               │
┌───▼────┐      ┌───▼─────┐   ┌────▼───┐
│Activity│      │ Rating  │   │  Data  │
│Service │      │ Service │   │Generator
└────────┘      └─────────┘   └────────┘
    │               │
    └───────────────┘
        ▼
┌─────────────────────────────────────────┐
│     Shared Libraries (common-*)         │
│  - DTOs, Clients, Domain Models         │
└─────────────────────────────────────────┘
```

### Service Communication

```
Service-A (REST call)
    ↓
common-clients dependency
    ↓
Generated HTTP client to Service-B
    ↓
Service-B responds (Mono/Flux)
    ↓
Response mapped to common-dto
```

### Data Access Layer (R2DBC + Reactive)

```
Controller (Mono/Flux)
    ↓
Service (business logic)
    ↓
Repository (R2DBC queries)
    ↓
MySQL (non-blocking driver)
    ↓
Mono<Entity> or Flux<Entity>
```

---

## 4. Core Patterns & Decisions

### Pattern 1: Reactive Streams (Mono/Flux)
- **Use When**: Async operations, database queries, HTTP calls
- **Mono<T>**: Single value or empty (like Optional)
- **Flux<T>**: Zero or more values (like Stream)
- **Rationale**: Non-blocking I/O enables high concurrency
- **Example**: `repository.findById(id)` returns `Mono<User>`

### Pattern 2: R2DBC Repository Pattern
- **Use When**: Database access
- **Interface**: Extend `ReactiveCrudRepository<T, ID>`
- **Rationale**: Async, non-blocking database operations
- **Example**: `userRepository.findById(id)` returns `Mono<User>`

### Pattern 3: Service-to-Service Communication
- **Use When**: One service needs data from another
- **Implementation**: Generated HTTP clients in `common-clients`
- **Rationale**: Decoupled services with clear contracts
- **Example**: `userClient.getUserById(id)` from `user-service`

### Pattern 4: DTO Sharing via Common Library
- **Use When**: APIs return complex objects
- **Implementation**: `common-dtos` library defines all DTOs
- **Rationale**: Single source of truth for data contracts
- **Example**: `UserDTO`, `CompanyDTO`, `ActionDTO`

### Pattern 5: Error Handling with Mono.error()
- **Use When**: Business logic errors
- **Implementation**: Custom exceptions, mapped to HTTP status
- **Rationale**: Reactive error propagation
- **Example**: `Mono.error(new UserNotFoundException(id))`

---

## 5. Service Registry & Responsibilities

### user-service
**Purpose**: User management, authentication, profiles  
**Models**: User, Role, Permission  
**Key Endpoints**: 
- `GET /users/:id` - Get user
- `POST /users` - Create user
- `PUT /users/:id` - Update user

### company-service
**Purpose**: Company/organization management  
**Models**: Company, Department, Team  
**Key Endpoints**:
- `GET /companies/:id`
- `POST /companies`
- `GET /companies/:id/teams`

### action-service
**Purpose**: Actions and workflow management  
**Models**: Action, ActionStatus, Workflow  
**Key Endpoints**:
- `GET /actions`
- `POST /actions`
- `PUT /actions/:id`

### activity-service
**Purpose**: Activity logging and user engagement  
**Models**: Activity, Metric, Statistics  
**Key Endpoints**:
- `GET /activities`
- `POST /activities`
- `GET /activities/stats`

### rating-service
**Purpose**: Rating and review system  
**Models**: Rating, Review, Score  
**Key Endpoints**:
- `GET /ratings/:id`
- `POST /ratings`
- `GET /ratings/avg`

### data-generator-service
**Purpose**: Test data generation and population  
**Key Endpoints**:
- `POST /generate/users`
- `POST /generate/companies`

---

## 6. Shared Libraries

### common-dtos
Contains all DTOs for inter-service communication:
- `UserDTO`, `UserResponse`
- `CompanyDTO`, `CompanyResponse`
- `ActionDTO`, `ActionRequest`
- Error response DTOs

### common-domains
Shared domain models and value objects:
- Base entities
- Enums (Status, Type, etc.)
- Common constants

### common-clients
HTTP clients for service-to-service calls:
- `UserServiceClient` - Calls user-service
- `CompanyServiceClient` - Calls company-service
- Generated from OpenAPI specifications
- Configured with proper timeouts and retries

---

## 7. API Specification

### REST Endpoints Pattern
```
/{service-name}/api/v1/{resource}

GET    /user-service/api/v1/users              - List users
GET    /user-service/api/v1/users/:id          - Get user
POST   /user-service/api/v1/users              - Create user
PUT    /user-service/api/v1/users/:id          - Update user
DELETE /user-service/api/v1/users/:id          - Delete user
```

### Response Format (Reactive)
```
{
  "success": true,
  "data": { /* payload */ },
  "timestamp": "2024-06-01T10:30:00Z",
  "requestId": "req-xxx-yyy-zzz"
}
```

### Error Responses
```
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "User not found",
    "timestamp": "2024-06-01T10:30:00Z"
  }
}
```

---

## 8. Database Schema

### Strategy
- Each service owns its schema/tables
- `common-dtos` defines shared models
- Migrations per service in `db/migrations/`

### Relationship Model
```
Users
  ├─ id (PK)
  ├─ email (unique)
  ├─ company_id (FK → Companies)
  └─ created_at

Companies
  ├─ id (PK)
  ├─ name
  └─ created_at

Actions
  ├─ id (PK)
  ├─ user_id (FK → Users)
  ├─ company_id (FK → Companies)
  └─ status
```

### Indexes
- `users(email)` - Login queries
- `actions(user_id, status)` - List by status
- `companies(name)` - Search

---

## 9. Testing Strategy

### Unit Tests
- **Framework**: JUnit 5
- **Coverage Target**: >85% on services
- **Approach**: Mock repositories, test business logic

### Integration Tests
- **Framework**: Testcontainers + MySQL
- **Database**: Real MySQL in Docker container
- **Approach**: Test repositories and full service stack

### E2E Tests (Optional)
- **Approach**: Test across multiple services
- **Database**: Real MySQL
- **Scope**: Critical workflows

### How to Run Tests
```bash
./gradlew test              # Unit tests
./gradlew testIntegration   # Integration tests
./gradlew test --coverage   # Coverage report
```

---

## 10. Deployment & Operations

### Environment Variables
| Variable | Purpose | Example |
|----------|---------|---------|
| `DATABASE_URL` | MySQL connection | `r2dbc:mysql://localhost:3306/fuel50` |
| `SERVICE_PORT` | Service port | `8081` |
| `LOG_LEVEL` | Logging level | `INFO` |
| `ENVIRONMENT` | Env (dev/staging/prod) | `production` |

### Running Services
```bash
./gradlew bootRun                 # Single service
./gradlew bootRun -args='--port=8081'  # Custom port

# Or after building JAR
java -jar user-service-1.0.jar
```

### Docker Compose
```yaml
# docker-compose.yml includes all services + MySQL
docker-compose up -d
```

---

## 11. Observability & Monitoring

### Metrics (Micrometer)
- Request count, latency, errors
- Database connection pool
- JVM metrics (memory, GC, threads)

### Logging
- Structured logging (JSON format)
- Request ID correlation across services
- Error stack traces to log aggregation

### Tracing
- Distributed tracing (OpenTelemetry)
- Trace context propagation
- Service-to-service call visibility

---

## 12. Scalability Characteristics

### Current Capacity (per service)
- **Throughput**: ~1000 requests/sec
- **Latency**: p50 ~20ms, p99 ~100ms
- **Concurrency**: 10,000+ virtual threads
- **Memory**: ~200-300MB per service

### Scaling Strategy
- Horizontal: Deploy multiple service instances
- Vertical: Increase container resources
- Database: Read replicas, connection pooling
- Caching: Redis for hot data (future)

---

## 13. Known Issues & Future Work

### Current Limitations
- [ ] No service mesh (Istio)
- [ ] No event-driven (Kafka)
- [ ] No distributed caching (Redis)
- [ ] Basic API gateway

### Planned Improvements
- [ ] Add Kafka for async events
- [ ] Implement Redis caching
- [ ] Add service mesh for resilience
- [ ] Implement circuit breaker (Resilience4j)
- [ ] Add distributed tracing (Jaeger)

---

## 14. File Structure Reference

```
fuel50/
├── apps/
│   ├── user-service/
│   │   ├── src/main/java/com/fuel50/user/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   │   └── UserServiceApplication.java
│   │   ├── build.gradle
│   │   └── pom.xml
│   ├── company-service/    # Similar structure
│   ├── action-service/     # Similar structure
│   └── [other services]/
├── libs/
│   ├── common-dtos/        # Shared DTOs
│   ├── common-domains/     # Shared models
│   └── common-clients/     # Service clients
├── database/               # DB setup scripts
├── kubernetes/             # K8s configs
├── docker-compose.yml
├── README.md
├── SPEC.md                 # This file
├── .instructions.md
├── .agent.md
└── build.gradle (root)
```

---

## References & Standards

- [Spring Boot WebFlux Documentation](https://spring.io/projects/spring-webflux)
- [Project Reactor Documentation](https://projectreactor.io/)
- [R2DBC Specification](https://r2dbc.io/)
- [Spring Cloud for Microservices](https://spring.io/cloud)
- [12-Factor App](https://12factor.net/)

---

**Version History**
- v1.0 (2024-06-01): Initial specification
