plugins {
	alias(libs.plugins.kotlin.jvm)
}

group = "org.munchies"

kotlin {
	jvmToolchain(21)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allprojects {
    repositories {
        mavenCentral()
    }
}
