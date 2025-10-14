package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.Path
import com.alex.munchies.util.STEP_ID
import com.alex.munchies.util.asStep
import com.alex.munchies.util.asSteps
import com.alex.munchies.util.postRecipe
import com.alex.munchies.util.postStep
import com.alex.munchies.util.shouldBeStep
import com.alex.munchies.util.shouldBeSteps
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StepControllerTest : BaseControllerTest() {

    private lateinit var recipeCreated: RecipeResponse

    @BeforeEach
    fun beforeEach() {
        // precondition to all tests: post a recipe
        recipeCreated = postRecipe(Fixtures.Recipes.pizza)
    }

    // region create

    @Test
    fun `should not create a step with invalid recipe-id`() {
        Given {
            body(Fixtures.Steps.dough)
        } When {
            post(Path.STEP, 999)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should create a step with valid request`() {
        val step = postStep(recipeCreated.id, Fixtures.Steps.dough)

        step.shouldNotBeNull()
        step shouldBeStep Fixtures.Steps.dough
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val steps = When {
            get(Path.STEP, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asSteps()
        }

        steps.shouldNotBeNull()
        steps shouldBe emptyList()
    }

    @Test
    fun `should return a list with one step`() {
        postStep(recipeCreated.id, Fixtures.Steps.dough)

        val steps = When {
            get(Path.STEP, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asSteps()
        }

        steps shouldHaveSize 1
        steps shouldBeSteps listOf(Fixtures.Steps.dough)
    }

    @Test
    fun `should return a list with ten steps`() {
        val stepsRequest = (1..10).map { Fixtures.Steps.dough.copy(number = it) }

        stepsRequest.forEach { postStep(recipeCreated.id, it) }

        val steps = When {
            get(Path.STEP, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asSteps()
        }

        steps shouldHaveSize 10
        steps shouldBeSteps stepsRequest
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid recipe-id`() {
        val stepPosted = postStep(recipeCreated.id, Fixtures.Steps.dough)

        When {
            get(Path.STEP_ID, 100, stepPosted.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should throw bad-request with invalid id`() {
        postStep(recipeCreated.id, Fixtures.Steps.dough)

        When {
            get(Path.STEP_ID, recipeCreated.id, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one step with valid id`() {
        val stepPosted = postStep(recipeCreated.id, Fixtures.Steps.dough)

        val step = When {
            get(Path.STEP_ID, recipeCreated.id, stepPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asStep()
        }

        step.shouldNotBeNull()
        step shouldBeStep Fixtures.Steps.dough
    }

    // endregion

}
