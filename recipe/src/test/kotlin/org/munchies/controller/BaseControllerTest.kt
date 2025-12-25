package org.munchies.controller

import org.munchies.configuration.SpringProfile
import org.munchies.repository.LabelRepository
import org.munchies.repository.RecipeRepository
import org.munchies.repository.StepRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.munchies.initializer.MySqlTestInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [MySqlTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
abstract class BaseControllerTest {

    @LocalServerPort
    private var port: Int = 0

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
    suspend fun afterEachBase() {
        labelRepository.deleteAll()
        recipeRepository.deleteAll()
        stepRepository.deleteAll()
    }
}
