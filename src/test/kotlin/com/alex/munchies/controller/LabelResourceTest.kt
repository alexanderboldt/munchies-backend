package com.alex.munchies.controller

import com.alex.munchies.repository.api.ApiModelLabel
import com.alex.munchies.repository.database.label.LabelRepository
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
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("tests")
class LabelResourceTest : BaseResourceTest() {

    @Autowired
    private lateinit var labelRepository: LabelRepository

    private object Labels {
        val vegetarian = ApiModelLabel(0, "", name = "Vegetarian", 1747138632, 1747138632)
        val vegan = ApiModelLabel(0, "", name = "Vegan", 1747138632, 1747138632)
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
            post(Routes.Label.main)
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
            get(Routes.Label.main)
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
            post(Routes.Label.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        }

        When {
            get(Routes.Label.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertLabel(Labels.vegetarian, true)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postLabel(Labels.vegetarian)

        When {
            get(Routes.Label.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postLabel(Labels.vegetarian)

        When {
            get(Routes.Label.detail, id)
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
            post(Routes.Label.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertLabel(label: ApiModelLabel, isInArray: Boolean = false) {
        val suffix = if (isInArray) "[0]" else ""

        body("id".plus(suffix), Matchers.greaterThan(0))
        body("userId".plus(suffix), equalTo(userId))
        body("name".plus(suffix), equalTo(label.name))
        body("createdAt".plus(suffix), Matchers.greaterThan(0L))
        body("updatedAt".plus(suffix), Matchers.greaterThan(0L))
    }
}