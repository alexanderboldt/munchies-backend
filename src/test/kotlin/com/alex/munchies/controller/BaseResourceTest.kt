package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe
import com.alex.munchies.extension.asLabel
import com.alex.munchies.extension.asRecipe
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
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
open class BaseResourceTest {

    @MockitoBean
    private lateinit var userService: UserService

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    protected lateinit var s3Service: S3Service

    @BeforeEach
    fun beforeEachBase() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        Mockito.`when`(userService.userId).thenReturn(Fixtures.User.USER_ID)
    }

    protected val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }

    protected fun createLabel(label: Label): Label {
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

    protected fun createRecipe(recipe: Recipe): Recipe {
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
