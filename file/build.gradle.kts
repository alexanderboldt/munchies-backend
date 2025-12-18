plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    // development libraries
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    implementation(libs.spring.context)

    implementation(libs.aws.s3)
}

tasks.test {
    useJUnitPlatform()
}
