package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Label
import com.alex.munchies.repository.label.LabelRepository
import io.restassured.common.mapper.TypeRef
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ResponseBodyExtractionOptions
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
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asLabel()
        }

        assertThat(label).isNotNull
        assertLabel(label, Fixtures.Labels.Domain.vegetarian)
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
            asLabels()
        }

        assertThat(labels).isNotNull
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
            asLabels()
        }

        assertThat(labels).isNotEmpty
        assertThat(labels).hasSize(1)
        assertLabels(labels, listOf(Fixtures.Labels.Domain.vegetarian))
    }

    @Test
    fun `should return a list with ten labels`() {
        val labelsRequest = (1..10).map { Fixtures.Labels.Domain.vegetarian }

        labelsRequest.forEach { postLabel(it) }

        val labels = When {
            get(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        assertThat(labels).isNotEmpty
        assertThat(labels).hasSize(10)
        assertLabels(labels, labelsRequest)
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
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
            asLabel()
        }

        assertThat(label).isNotNull
        assertLabel(label, Fixtures.Labels.Domain.vegetarian)
    }

    // endregion

    // region update

    @Test
    fun `should not update a label and throw bad-request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
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
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabel()
        }

        assertThat(label).isNotNull
        assertLabel(label, Fixtures.Labels.Domain.vegan)
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a label and throw bad-request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            delete(Routes.Label.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a label with valid id`() {
        val id = postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            delete(Routes.Label.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    fun postLabel(label: Label): Int {
        return Given {
            body(label)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ResponseBodyExtractionOptions.asLabels() = `as`(object : TypeRef<List<Label>>() {})
    private fun ResponseBodyExtractionOptions.asLabel() = `as`(object : TypeRef<Label>() {})

    private fun assertLabels(labelsActual: List<Label>, labelsOther: List<Label>) {
        labelsActual.zip(labelsOther).forEach { (labelActual, labelOther) ->
            assertLabel(labelActual, labelOther)
        }
    }

    private fun assertLabel(labelActual: Label, labelOther: Label) {
        assertThat(labelActual.id).isGreaterThan(0)
        assertThat(labelActual.userId).isEqualTo(Fixtures.User.USER_ID)
        assertThat(labelActual.name).isEqualTo(labelOther.name)
        assertThat(labelActual.createdAt).isGreaterThan(0)
        assertThat(labelActual.updatedAt).isGreaterThan(0)
    }
}
