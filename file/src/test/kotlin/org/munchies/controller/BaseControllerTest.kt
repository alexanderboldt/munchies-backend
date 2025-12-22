package org.munchies.controller

import org.munchies.initializer.MinioTestInitializer
import org.munchies.service.S3Service
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [MinioTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tests")
abstract class BaseControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    protected lateinit var s3Service: S3Service

    @BeforeEach
    fun beforeEachBase() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }
}
