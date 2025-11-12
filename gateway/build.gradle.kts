plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.cloud.starter.gateway)

    implementation(libs.spring.boot.starter.oauth2.resource)
    implementation(libs.spring.boot.starter.security)

    // test libraries
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "junit")
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}
