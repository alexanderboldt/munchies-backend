package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Label
import com.alex.munchies.repository.label.LabelRepository
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
class LabelResourceTest : BaseResourceTest() {

    @Autowired
    private lateinit var labelRepository: LabelRepository

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
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertLabel(Fixtures.Labels.Domain.vegetarian)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoLabels() {
        When {
            get(Routes.Label.MAIN)
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
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        }

        When {
            get(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertLabel(Fixtures.Labels.Domain.vegetarian, true)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            get(Routes.Label.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            get(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertLabel(Fixtures.Labels.Domain.vegetarian)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Routes.Label.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testUpdateWithValidId() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertLabel(Fixtures.Labels.Domain.vegan)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Label.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testDeleteWithValidLabel() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    fun postLabel(label: Label): Int {
        return Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(label)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertLabel(label: Label, isInArray: Boolean = false) {
        val suffix = if (isInArray) "[0]" else ""

        body("id".plus(suffix), Matchers.greaterThan(0))
        body("userId".plus(suffix), equalTo(Fixtures.User.USER_ID))
        body("name".plus(suffix), equalTo(label.name))
        body("createdAt".plus(suffix), Matchers.greaterThan(0L))
        body("updatedAt".plus(suffix), Matchers.greaterThan(0L))
    }
}