plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.cloud.starter.gateway.server.webflux)

    implementation(libs.spring.boot.starter.security.oauth2.resource.server)
    implementation(libs.spring.boot.starter.data.redis.reactive)

    implementation(project(":common"))

    // test libraries
    testImplementation(libs.spring.boot.starter.webmvc.test) {
        exclude(module = "junit")
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}
