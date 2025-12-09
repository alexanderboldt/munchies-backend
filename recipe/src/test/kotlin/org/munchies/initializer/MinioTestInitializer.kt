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
            "spring.s3.endpoint-override=${minio.s3URL}",
            "spring.s3.aws.credentials.static-provider.access-key-id=${minio.userName}",
            "spring.s3.aws.credentials.static-provider.secret-access-key=${minio.password}",
        ).applyTo(context.environment)
    }
}
