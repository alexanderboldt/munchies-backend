package com.alex.munchies.controller

import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelLabel
import com.alex.munchies.repository.database.label.LabelRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("tests")
class LabelResourceTest {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            RestAssured.baseURI = "http://localhost"
        }
    }

    @LocalServerPort
    private var port: Int = 0

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var labelRepository: LabelRepository

    private val userId = "12345"

    private object Routes {
        val main = "/api/v1/labels"
        val detail = "$main/{id}"
    }

    private object Labels {
        val vegetarian = ApiModelLabel(0, "", name = "Vegetarian", 1747138632, 1747138632)
        val vegan = ApiModelLabel(0, "", name = "Vegan", 1747138632, 1747138632)
    }

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port

        Mockito.`when`(userService.userId).thenReturn(userId)
    }

    @AfterEach
    fun afterEach() {
        labelRepository.deleteAll()
    }

    // region create

    @Test
    fun testPostWithValidRequest() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Labels.vegetarian)
        } When {
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertLabel(Labels.vegetarian)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoLabels() {
        When {
            get(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneLabel() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Labels.vegetarian)
        } When {
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        }

        When {
            get(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertLabelInArray(Labels.vegetarian)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postLabel(Labels.vegetarian)

        When {
            get(Routes.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postLabel(Labels.vegetarian)

        When {
            get(Routes.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertLabel(Labels.vegetarian)
        }
    }

    // endregion

    fun postLabel(label: ApiModelLabel): Int {
        return Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(label)
        } When {
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertLabel(label: ApiModelLabel) {
        body("id", Matchers.greaterThan(0))
        body("userId", equalTo(userId))
        body("name", equalTo(label.name))
        body("createdAt", Matchers.greaterThan(0L))
        body("updatedAt", Matchers.greaterThan(0L))
    }

    private fun ValidatableResponse.assertLabelInArray(label: ApiModelLabel) {
        body("id[0]", Matchers.greaterThan(0))
        body("userId[0]", equalTo(userId))
        body("name[0]", equalTo(label.name))
        body("createdAt[0]", Matchers.greaterThan(0L))
        body("updatedAt[0]", Matchers.greaterThan(0L))
    }
}