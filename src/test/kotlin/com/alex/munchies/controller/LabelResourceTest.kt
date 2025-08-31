package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.util.asLabel
import com.alex.munchies.util.asLabels
import com.alex.munchies.util.LABEL_ID
import com.alex.munchies.util.Path
import com.alex.munchies.util.shouldBeLabel
import com.alex.munchies.util.shouldBeLabels
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class LabelResourceTest : BaseResourceTest() {

    // region create

    @Test
    fun `should create a label with valid request`() {
        val label = postLabel(Fixtures.Labels.Domain.vegetarian)

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.Domain.vegetarian
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val labels = When {
            get(Path.LABEL)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        labels.shouldNotBeNull()
        labels shouldBe emptyList()
    }

    @Test
    fun `should return a list with one label`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        val labels = When {
            get(Path.LABEL)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabels()
        }

        labels shouldHaveSize 1
        labels shouldBeLabels listOf(Fixtures.Labels.Domain.vegetarian)
    }

    @Test
    fun `should return a list with ten labels`() {
        val labelsRequest = (1..10).map { Fixtures.Labels.Domain.vegetarian }

        labelsRequest.forEach { postLabel(it) }

        val labels = When {
            get(Path.LABEL)
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
    fun `should throw bad-request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            get(Path.LABEL_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one label with valid id`() {
        val labelPosted = postLabel(Fixtures.Labels.Domain.vegetarian)

        val label = When {
            get(Path.LABEL_ID, labelPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabel()
        }

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.Domain.vegetarian
    }

    // endregion

    // region update

    @Test
    fun `should not update a label and throw bad-request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        Given {
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Path.LABEL_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return a label with valid id`() {
        val labelPosted = postLabel(Fixtures.Labels.Domain.vegetarian)

        val label = Given {
            body(Fixtures.Labels.Domain.vegan)
        } When {
            put(Path.LABEL_ID, labelPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asLabel()
        }

        label.shouldNotBeNull()
        label shouldBeLabel Fixtures.Labels.Domain.vegan
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a label and throw bad-request with invalid id`() {
        postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            delete(Path.LABEL_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a label with valid id`() {
        val labelPosted = postLabel(Fixtures.Labels.Domain.vegetarian)

        When {
            delete(Path.LABEL_ID, labelPosted.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
