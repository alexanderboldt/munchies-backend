# stage 1: build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean gateway:bootJar

# stage 2: run
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/gateway/build/libs/gateway.jar app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"]
