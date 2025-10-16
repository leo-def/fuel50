# syntax=docker/dockerfile:1

FROM gradle:8.7-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :apps:user-service:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY apps/user-service/build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app/app.jar"]