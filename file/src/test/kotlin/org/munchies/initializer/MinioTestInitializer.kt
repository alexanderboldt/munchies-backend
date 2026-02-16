package org.munchies.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.junit.jupiter.Container

class MinioTestInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private val minio = MinIOContainer("minio/minio:latest")
        .withUserName("minio-test")
        .withPassword("minio-test")

    override fun initialize(context: ConfigurableApplicationContext) {
        minio.start()

        TestPropertyValues.of(
            "s3.host=${minio.host}",
            "s3.port=${minio.firstMappedPort}",
            "s3.access-key-id=${minio.userName}",
            "s3.secret-access-key=${minio.password}"
        ).applyTo(context.environment)
    }
}
