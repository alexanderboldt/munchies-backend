# Munchies

This is a multi-module backend application with microservice architecture. Developed with Kotlin and Spring Boot for managing recipes.

## Tech-Stack

### Gateway Microservice
- Kotlin
- Spring Cloud Gateway
- Redis
- Keycloak

### Recipe Microservice
- Kotlin
- Spring Boot
- Kotlin Coroutines with WebFlux
- Hibernate
- MySQL
- Flyway
- OpenApi

### File Microservice
- Kotlin
- Spring Boot
- Kotlin Coroutines with WebFlux
- MinIO

### Test Environment
- RestAssured
- Mockito
- Kotest
- Docker-Testcontainer

### Buildsystem
- Gradle

## Install
1. Build the docker image of the gateway microservice:
```bash
./gradlew clean gateway:jibDockerBuild
```
2. Build the docker image of the recipe microservice:
```bash
./gradlew clean recipe:jibDockerBuild
```
3. Build the docker image of the file microservice:
```bash
./gradlew clean file:jibDockerBuild
```
4. Create and start the containers:
```bash
docker compose up -d
```
