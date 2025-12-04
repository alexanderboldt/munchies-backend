plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.jib)
}

dependencies {
	// development libraries
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlin.stdlib)
	implementation(libs.jackson.kotlin)

	implementation(libs.spring.boot.starter.webmvc)
	implementation(libs.spring.boot.starter.data)
	implementation(libs.spring.boot.starter.flyway)

	implementation(libs.mysql)
    implementation(libs.flyway.mysql)

    implementation(libs.aws.s3)

	implementation(libs.springdoc)

    implementation(project(":common"))

	// test libraries
	testImplementation(libs.spring.boot.starter.webmvc.test) { exclude(module = "junit") }
    testImplementation(libs.spring.boot.starter.flyway.test)

    testImplementation(libs.mockito.kotlin)

	testImplementation(libs.kotest.runner.junit)
	testImplementation(libs.kotest.assertions.core)

    testImplementation(libs.rest.assured)

	testImplementation(libs.testcontainers.junit.jupiter)
	testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.minio)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// run with: ./gradlew clean recipe:jibDockerBuild
jib {
    from.image = "eclipse-temurin:21-jdk-alpine"
    to.image = "munchies/recipe:5.0.0"
}
