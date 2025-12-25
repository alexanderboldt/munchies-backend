package org.munchies.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.mysql.MySQLContainer

class MySqlTestInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private val mysql = MySQLContainer("mysql:9.2.0")
        .withDatabaseName("munchies")
        .withUsername("admin")
        .withPassword("admin")

    override fun initialize(context: ConfigurableApplicationContext) {
        mysql.start()

        TestPropertyValues.of(
            "spring.flyway.url=${mysql.jdbcUrl}",
            "spring.flyway.user=${mysql.username}",
            "spring.flyway.password=${mysql.password}",
            "spring.r2dbc.url=${"r2dbc:mysql://${mysql.host}:${mysql.firstMappedPort}/${mysql.databaseName}" }",
            "spring.r2dbc.username=${mysql.username}",
            "spring.r2dbc.password=${mysql.password}"
        ).applyTo(context.environment)
    }
}
