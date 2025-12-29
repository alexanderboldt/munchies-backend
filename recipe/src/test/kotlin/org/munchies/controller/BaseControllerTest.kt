package org.munchies.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.just
import io.mockk.runs
import org.munchies.configuration.SpringProfile
import org.munchies.repository.LabelRepository
import org.munchies.repository.RecipeRepository
import org.munchies.repository.StepRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.munchies.Fixtures
import org.munchies.S3Bucket
import org.munchies.client.FileClient
import org.munchies.initializer.MySqlTestInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [MySqlTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
abstract class BaseControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @MockkBean
    lateinit var fileClient: FileClient

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

        // mock the FileClient for all tests
        coEvery { fileClient.upload(any(), any()) } returns Fixtures.fileResponse
        coEvery { fileClient.download(S3Bucket.RECIPE, Fixtures.fileResponse.filename) } returns flowOf(DefaultDataBufferFactory().wrap(Fixtures.image.readBytes()))
        coEvery { fileClient.delete(any(), any()) } just runs
    }

    @AfterEach
    suspend fun afterEachBase() {
        labelRepository.deleteAll()
        recipeRepository.deleteAll()
        stepRepository.deleteAll()
    }
}
