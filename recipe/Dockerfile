# stage 1: build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean recipe:bootJar

# stage 2: run
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/recipe/build/libs/recipe.jar app.jar
EXPOSE 4001
ENTRYPOINT ["java", "-jar", "app.jar"]
