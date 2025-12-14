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
- MinIO
- OpenApi

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
3. Create and start the containers:
```bash
docker compose up -d
```
