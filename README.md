Fuel50 Microservices

Overview
- Reactive Spring Boot microservices using WebFlux and R2DBC (MySQL).
- Services: `company-service`, `user-service`, `activity-service`, `action-service`, `rating-service`, `data-generator-service`.
- Shared libs: `libs/common-dtos`, `libs/common-domains`, `libs/common-clients`.
- OpenAPI via Springdoc: each service exposes `/v3/api-docs` and `swagger-ui`.

Prerequisites
- Java 21 (JDK): required by Gradle and Spring Boot 3.3.
- Docker and Docker Compose: for local infra and service orchestration.
- Kubernetes cluster and `kubectl` (optional): for k8s deployment; an Ingress controller if you want a single entrypoint.

Project Structure
- `apps/*`: microservices.
- `libs/*`: shared libraries.
- `database/schema.sql`: schema mounted into MySQL in Docker Compose.
- `k8s/stack.yaml`: Kubernetes manifest for DB, Ollama, and services.
- `.env.example`: environment template used by Docker Compose.

Setup Environment
- Copy defaults: `cp .env.example .env`
- Edit `.env` if needed (ports, DB credentials, Ollama model, client base URLs).

Run Services Locally (isolated)
- Start MySQL only (via Compose): `docker compose up -d db`
- Export env for the service you’re running (example):
  - `export SPRING_R2DBC_URL="r2dbc:mysql://localhost:3306/fuel50"`
  - `export SPRING_R2DBC_USERNAME="app_user"`
  - `export SPRING_R2DBC_PASSWORD="app_password"`
  - `export SPRING_SQL_INIT_MODE="never"`
  - `export SERVER_PORT="8082"` (change per service if desired)
- Start a service via Gradle:
  - Company: `./gradlew :apps:company-service:bootRun`
  - User: `./gradlew :apps:user-service:bootRun`
  - Activity: `./gradlew :apps:activity-service:bootRun`
  - Action: `./gradlew :apps:action-service:bootRun`
  - Rating: `./gradlew :apps:rating-service:bootRun`
  - Data Generator: `./gradlew :apps:data-generator-service:bootRun`

Run Everything with Docker Compose
- Build and run: `docker compose up --build`
- Default ports (from `.env.example`):
  - `company-service`: `http://localhost:8082`
  - `user-service`: `http://localhost:8085`
  - `activity-service`: `http://localhost:8086`
  - `action-service`: `http://localhost:8081`
  - `rating-service`: `http://localhost:8084`
  - `data-generator-service`: `http://localhost:8083`
- MySQL: `localhost:3306` (db container)
- Ollama API: `http://localhost:11434`

Debugging
- Recommended: run dependencies in Compose; run the target service locally and attach your IDE debugger (hybrid).
- Start dependencies (excluding the service you’ll debug):
  - `docker compose up -d db ollama ollama-init company-service user-service activity-service action-service rating-service`
- Ensure Compose publishes ports for dependencies:
  - If `activity-service` is not published, add to `docker-compose.yml` under `activity-service`:
    - `ports:` then `- "${ACTIVITY_SERVICE_PORT}:${ACTIVITY_SERVICE_PORT}"`
- Configure environment for local `data-generator-service` to call Compose services:
  - `export CLIENTS_COMPANY_BASE_URL="http://localhost:8082"`
  - `export CLIENTS_USER_BASE_URL="http://localhost:8085"`
  - `export CLIENTS_ACTIVITY_BASE_URL="http://localhost:8086"`
  - `export CLIENTS_ACTION_BASE_URL="http://localhost:8081"`
  - `export CLIENTS_USER_ACTIVITY_BASE_URL="http://localhost:8086"`
  - `export SPRING_AI_OLLAMA_BASE_URL="http://localhost:11434/"`
- Run the target service locally and attach debugger:
  - `./gradlew :apps:data-generator-service:bootRun`
  - IntelliJ: Use “Attach to process” or run with debugger; VS Code: Java attach configuration.
- Alternative: remote debug inside Docker Compose for any service:
  - In `docker-compose.yml`, add to the service:
    - `environment:` `JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n`
    - `ports:` `5005:5005`
  - Attach your IDE to `localhost:5005`. Use `suspend=y` to pause on startup.
- Local-only quick iteration:
  - Run a single service with `bootRun`; optionally start only DB via Compose: `docker compose up -d db`.
- WebFlux/Reactor tips:
  - Set breakpoints at controller/service boundaries; async chains won’t have linear stacks.
  - Optional logging: `logging.level.reactor.netty=DEBUG`, `logging.level.org.springframework.web.reactive.function.client=DEBUG`.
- Scripted helper:
  - Use `bash scripts/debug-service.sh <service>` to start all dependencies in Docker Compose and run the selected service locally.
  - Example for data-generator: `bash scripts/debug-service.sh data-generator-service`
  - Optional remote debug agent: `bash scripts/debug-service.sh data-generator-service --jdwp 5005 --suspend`
 - Per-service simple scripts:
   - `bash scripts/debug-company-service.sh`
   - `bash scripts/debug-user-service.sh`
   - `bash scripts/debug-activity-service.sh`
   - `bash scripts/debug-action-service.sh`
   - `bash scripts/debug-rating-service.sh`
   - `bash scripts/debug-data-generator-service.sh`
   - These wrap the generic script and accept the same optional flags (e.g., `--jdwp 5005`).

Swagger UI and API Docs
- Swagger UI: `http://localhost:<PORT>/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:<PORT>/v3/api-docs`
- Examples:
  - Company: `http://localhost:8082/swagger-ui/index.html`
  - User: `http://localhost:8085/swagger-ui/index.html`
  - Activity: `http://localhost:8086/swagger-ui/index.html`
  - Action: `http://localhost:8081/swagger-ui/index.html`
  - Rating: `http://localhost:8084/swagger-ui/index.html`
  - Data Generator: `http://localhost:8083/swagger-ui/index.html`

Quick Endpoint Examples (curl)
- Create Company:
  - `curl -X POST http://localhost:8082/company -H 'Content-Type: application/json' -d '{"name":"Acme"}'`
- List Users in a Company:
  - `curl http://localhost:8085/company/1/user`
- Create Activity in Company:
  - `curl -X POST http://localhost:8086/company/1/activity -H 'Content-Type: application/json' -d '{"name":"Onboarding"}'`
- Link User to Activity:
  - `curl -X POST http://localhost:8086/company/1/user/5/activity/100/user-activity -H 'Content-Type: application/json' -d '{"userId":5,"activityId":100}'`
- Create Rating:
  - `curl -X POST http://localhost:8084/rating -H 'Content-Type: application/json' -d '{"userActivityId":200,"score":5}'`

Kubernetes Deployment
- Images: push your service images to a registry accessible by the cluster (e.g., `ghcr.io/your-org/company-service:latest`).
- Update `image:` fields in `k8s/stack.yaml` accordingly.
- Apply stack: `kubectl apply -f k8s/stack.yaml`
- Namespace: resources deploy to `fuel50`.
- Access services:
  - Port-forward example: `kubectl -n fuel50 port-forward svc/company-service 8082:8080`
  - Repeat for other services using their local ports mapped to service port 8080.
- Optional Ingress:
  - If you install an Ingress controller (e.g., NGINX), create an Ingress to route `/company`, `/user`, `/activity`, `/action`, `/rating`, `/prompt` to corresponding services.

Data Generator Service Notes
- Requires Ollama API reachable at `SPRING_AI_OLLAMA_BASE_URL`.
- Client base URLs should point to microservice services (Compose: container names and ports; K8s: service names on port 8080):
  - K8s example: `CLIENTS_COMPANY_BASE_URL=http://company-service:8080`, etc.

Build and Test
- Build JARs: `./gradlew build`
- Run unit tests: `./gradlew test`

 Troubleshooting
 - If Swagger UI doesn’t load, ensure `springdoc` is included and `springdoc.swagger-ui.enabled=true`.
 - DB connection errors: verify `SPRING_R2DBC_URL/USERNAME/PASSWORD` and MySQL health.
 - K8s image pull errors: set your registry, push images, or use `minikube image load` for local clusters.

Automation Helpers
- Generate `.env` from `.env.example`:
  - Compose hosts: `./scripts/bootstrap-env.sh`
  - Local hosts (`localhost`): `./scripts/bootstrap-env.sh --mode local`
- Run services using env from `.env` (minimal scripts):
  - `./scripts/debug-company-service.sh`
  - `./scripts/debug-user-service.sh`
  - `./scripts/debug-activity-service.sh`
  - `./scripts/debug-action-service.sh`
  - `./scripts/debug-rating-service.sh`
  - `./scripts/debug-data-generator-service.sh`
  - Each script exports required env vars and runs Gradle.