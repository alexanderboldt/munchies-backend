plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
	// development libraries
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlin.stdlib)
	implementation(libs.jackson.kotlin)

	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.data)
	implementation(libs.spring.boot.starter.aop)

	implementation(libs.mysql)
	implementation(libs.flyway.core)
	implementation(libs.flyway.mysql)

	implementation(libs.openfeign)

    implementation(libs.aws.s3)

    implementation(libs.resilience4j)

	implementation(libs.springdoc)

	// test libraries
	testImplementation(libs.spring.boot.starter.test) {
		exclude(module = "junit")
	}

    testImplementation(libs.mockito.kotlin)

	testImplementation(libs.kotest.runner.junit)
	testImplementation(libs.kotest.assertions.core)

	testImplementation(libs.testcontainers.junit.jupiter)

	testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.minio)
	testImplementation(libs.rest.assured)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
