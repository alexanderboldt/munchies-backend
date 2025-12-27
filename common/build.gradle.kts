plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // development libraries
    implementation(libs.spring.web)
}

tasks.test {
    useJUnitPlatform()
}
