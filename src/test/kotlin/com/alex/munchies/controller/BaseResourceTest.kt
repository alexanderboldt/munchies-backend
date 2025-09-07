package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe
import com.alex.munchies.initializer.MinioTestInitializer
import com.alex.munchies.util.asLabel
import com.alex.munchies.util.asRecipe
import com.alex.munchies.repository.LabelRepository
import com.alex.munchies.repository.RecipeRepository
import com.alex.munchies.service.S3Service
import com.alex.munchies.service.UserService
import com.alex.munchies.util.Path
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.io.File

@ContextConfiguration(initializers = [MinioTestInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
open class BaseResourceTest {

    @MockitoBean
    private lateinit var userService: UserService

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    protected lateinit var s3Service: S3Service

    @Autowired
    protected lateinit var labelRepository: LabelRepository

    @Autowired
    protected lateinit var recipeRepository: RecipeRepository

    @BeforeEach
    fun beforeEachBase() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        Mockito.`when`(userService.userId).thenReturn(Fixtures.User.USER_ID)
    }

    @AfterEach
    fun afterEachBase() {
        labelRepository.deleteAll()
        recipeRepository.deleteAll()
    }

    protected val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }

    protected fun postLabel(label: Label): Label {
        return Given {
            body(label)
        } When {
            post(Path.LABEL)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asLabel()
        }
    }

    protected fun postRecipe(recipe: Recipe): Recipe {
        return Given {
            body(recipe)
        } When {
            post(Path.RECIPE)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asRecipe()
        }
    }

    protected fun uploadImage(id: Long): Recipe {
        return Given {
            multiPart("image", image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.RECIPE_IMAGE, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }
    }
}
