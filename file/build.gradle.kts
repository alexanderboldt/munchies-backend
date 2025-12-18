plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
}

tasks.test {
    useJUnitPlatform()
}
