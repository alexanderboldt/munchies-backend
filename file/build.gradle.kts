plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.jib)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    implementation(libs.jackson.kotlin)

    implementation(libs.spring.boot.starter.webflux)

    implementation(libs.aws.s3)

    implementation(project(":common"))

    // test libraries
    testImplementation(libs.spring.boot.starter.webflux.test) { exclude(module = "junit") }

    testImplementation(libs.kotest.runner.junit)
    testImplementation(libs.kotest.assertions.core)

    testImplementation(libs.rest.assured)

    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.minio)
}

tasks.test {
    useJUnitPlatform()
}

// run with: ./gradlew clean file:jibDockerBuild
jib {
    from.image = "eclipse-temurin:21-jdk-alpine"
    to.image = "munchies/file:1.0.0"
}
