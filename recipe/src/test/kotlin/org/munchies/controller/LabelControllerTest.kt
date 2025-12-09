package org.munchies.controller

import org.munchies.Fixtures
import org.munchies.Header
import org.munchies.Path
import org.munchies.util.asLabel
import org.munchies.util.asLabels
import org.munchies.util.createLabel
import org.munchies.util.shouldBeLabel
import org.munchies.util.shouldBeLabels
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class LabelControllerTest : BaseControllerTest() {

    // region create

    @Test
    fun `should create a label with valid request`() {
        val label = createLabel(Fixtures.Labels.vegetarian)

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.vegetarian
    }

    // endregion

    // region read all

    @Test
    fun `should read all labels and return an empty list`() {
        val labels = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.LABELS)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        labels.shouldNotBeNull()
        labels shouldBe emptyList()
    }

    @Test
    fun `should read all labels and return a list with one label`() {
        createLabel(Fixtures.Labels.vegetarian)

        val labels = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.LABELS)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        labels shouldHaveSize 1
        labels shouldBeLabels listOf(Fixtures.Labels.vegetarian)
    }

    @Test
    fun `should read all labels and return a list with ten labels`() {
        val labelsRequest = (1..10).map { Fixtures.Labels.vegetarian }

        labelsRequest.forEach { createLabel(it) }

        val labels = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.LABELS)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        labels shouldHaveSize 10
        labels shouldBeLabels labelsRequest
    }

    // endregion

    // region read one

    @Test
    fun `should not read one label and throw bad-request with invalid id`() {
        createLabel(Fixtures.Labels.vegetarian)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.LABELS_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should read one label and return it with valid id`() {
        val labelCreated = createLabel(Fixtures.Labels.vegetarian)

        val label = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.LABELS_ID, labelCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabel()
        }

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.vegetarian
    }

    // endregion

    // region update

    @Test
    fun `should not update a label and throw bad-request with invalid id`() {
        createLabel(Fixtures.Labels.vegetarian)

        Given {
            body(Fixtures.Labels.vegan)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.LABELS_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update a label and return it with valid id`() {
        val labelCreated = createLabel(Fixtures.Labels.vegetarian)

        val label = Given {
            body(Fixtures.Labels.vegan)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.LABELS_ID, labelCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabel()
        }

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.vegan
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a label and throw bad-request with invalid id`() {
        createLabel(Fixtures.Labels.vegetarian)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.LABELS_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a label with valid id`() {
        val labelCreated = createLabel(Fixtures.Labels.vegetarian)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.LABELS_ID, labelCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
