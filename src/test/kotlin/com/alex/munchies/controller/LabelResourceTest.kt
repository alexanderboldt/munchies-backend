package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Label
import com.alex.munchies.repository.label.LabelRepository
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
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
    fun `should create a label with valid request`() {
        val label = Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            `as`(object : TypeRef<Label>() {})
        }

        assertThat(label).isNotNull
        assertLabel(label)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val labels = When {
            get(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            `as`(object : TypeRef<List<Label>>() {})
        }

        assertThat(labels).isEmpty()
    }

    @Test
    fun `should return a list with one label`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        val labels = When {
            get(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            `as`(object : TypeRef<List<Label>>() {})
        }

        assertThat(labels).isNotEmpty
        assertThat(labels).hasSize(1)
        assertLabels(labels)
    }

    @Test
    fun `should return a list with ten labels`() {
        (1..10).forEach { _ ->
            postLabel(Fixtures.Labels.Domain.vegetarian)
        }

        val labels = When {
            get(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            `as`(object : TypeRef<List<Label>>() {})
        }

        assertThat(labels).isNotEmpty
        assertThat(labels).hasSize(10)
        assertLabels(labels)
    }

    // endregion

    // region read one

    @Test
    fun `should return bad request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            get(Routes.Label.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one label with valid id`() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        val label = When {
            get(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            `as`(object : TypeRef<Label>() {})
        }

        assertThat(label).isNotNull
        assertLabel(label)
    }

    // endregion

    // region update

    @Test
    fun `should not update a label and return bad request with invalid id`() {
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
    fun `should update and return a label with valid id`() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        val label = Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            `as`(object : TypeRef<Label>() {})
        }

        assertThat(label).isNotNull
        assertLabel(label)
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a label with invalid id`() {
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
    fun `should delete a label with valid id`() {
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

    private fun assertLabels(labels: List<Label>) {
        labels.forEach { assertLabel(it) }
    }

    private fun assertLabel(label: Label) {
        assertThat(label.id).isGreaterThan(0)
        assertThat(label.userId).isEqualTo(Fixtures.User.USER_ID)
        assertThat(label.name).isNotBlank
        assertThat(label.createdAt).isGreaterThan(0)
        assertThat(label.updatedAt).isGreaterThan(0)
    }
}