plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.cloud.starter.gateway.server.webflux)

    implementation(libs.spring.boot.starter.security.oauth2.resource.server)
    implementation(libs.spring.boot.starter.data.redis.reactive)

    implementation(project(":common"))

    // test libraries
    testImplementation(libs.spring.boot.starter.webflux.test) {
        exclude(module = "junit")
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// run with: ./gradlew clean gateway:jibDockerBuild
jib {
    from.image = "eclipse-temurin:21-jdk-alpine"
    to.image = "munchies/gateway:2.1.0"
}
