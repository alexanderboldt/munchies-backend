package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.initializer.MinioTestInitializer
import com.alex.munchies.repository.LabelRepository
import com.alex.munchies.repository.RecipeRepository
import com.alex.munchies.service.S3Service
import com.alex.munchies.service.UserService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@ContextConfiguration(initializers = [MinioTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
abstract class BaseControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    protected lateinit var s3Service: S3Service

    @Autowired
    private lateinit var labelRepository: LabelRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @BeforeEach
    fun beforeEachBase() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        whenever(userService.userId).doReturn(Fixtures.User.USER_ID)
    }

    @AfterEach
    fun afterEachBase() {
        labelRepository.deleteAll()
        recipeRepository.deleteAll()
    }
}
