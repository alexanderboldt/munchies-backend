package com.alex.munchies.controller

import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.initializer.MinioTestInitializer
import com.alex.munchies.repository.LabelRepository
import com.alex.munchies.repository.RecipeRepository
import com.alex.munchies.repository.StepRepository
import com.alex.munchies.service.S3Service
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [MinioTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
abstract class BaseControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    protected lateinit var s3Service: S3Service

    @Autowired
    private lateinit var labelRepository: LabelRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var stepRepository: StepRepository

    @BeforeEach
    fun beforeEachBase() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }

    @AfterEach
    fun afterEachBase() {
        labelRepository.deleteAll()
        recipeRepository.deleteAll()
        stepRepository.deleteAll()
    }
}
