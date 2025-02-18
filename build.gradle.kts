plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.kotlin.jpa)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
}

group = "com.alex"
version = "0.0.1-SNAPSHOT"

kotlin {
	jvmToolchain(23)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// project-libraries
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlin.stdlib)
	implementation(libs.jackson.kotlin)

	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.data)
	implementation(libs.spring.boot.starter.oauth2.client)
	implementation(libs.spring.boot.starter.oauth2.resource)
	implementation(libs.spring.boot.starter.security)

	implementation(libs.mysql)
	implementation(libs.flyway.core)
	implementation(libs.flyway.mysql)

	implementation(libs.openfeign)

	developmentOnly(libs.spring.boot.devtools)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
