package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.Header
import com.alex.munchies.Path
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.asStep
import com.alex.munchies.util.asSteps
import com.alex.munchies.util.createRecipe
import com.alex.munchies.util.createStep
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

class RecipeStepControllerTest : BaseControllerTest() {

    private lateinit var recipeCreated: RecipeResponse

    @BeforeEach
    fun beforeEach() {
        // precondition to all tests: create a recipe
        recipeCreated = createRecipe(Fixtures.Recipes.pizza)
    }

    // region create

    @Test
    fun `should not create a step with invalid recipe-id`() {
        Given {
            body(Fixtures.Steps.dough)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            post(Path.RECIPES_STEPS, 999)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should create a step with valid request`() {
        val step = createStep(recipeCreated.id, Fixtures.Steps.dough)

        step.shouldNotBeNull()
        step shouldBeStep Fixtures.Steps.dough
    }

    // endregion

    // region read all

    @Test
    fun `should read all steps and return an empty list`() {
        val steps = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asSteps()
        }

        steps.shouldNotBeNull()
        steps shouldBe emptyList()
    }

    @Test
    fun `should read all steps and return a list with one step`() {
        createStep(recipeCreated.id, Fixtures.Steps.dough)

        val steps = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asSteps()
        }

        steps shouldHaveSize 1
        steps shouldBeSteps listOf(Fixtures.Steps.dough)
    }

    @Test
    fun `should read all steps and return a list with ten steps`() {
        val stepsRequest = (1..10).map { Fixtures.Steps.dough.copy(number = it) }

        stepsRequest.forEach { createStep(recipeCreated.id, it) }

        val steps = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS, recipeCreated.id)
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
    fun `should not read one step and throw bad-request with invalid recipe-id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS_ID, 100, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not read one step and throw bad-request with invalid id`() {
        createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS_ID, recipeCreated.id, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should read one step and return it with valid id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        val step = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS_ID, recipeCreated.id, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asStep()
        }

        step.shouldNotBeNull()
        step shouldBeStep Fixtures.Steps.dough
    }

    // endregion

    // region update

    @Test
    fun `should not update a step and throw bad-request with invalid recipe-id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            body(Fixtures.Steps.sauce)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.RECIPES_STEPS_ID, 100, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not update a step and throw bad-request with invalid id`() {
        createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            body(Fixtures.Steps.sauce)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.RECIPES_STEPS_ID, recipeCreated.id, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update a step and return it with valid id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        val step = Given {
            body(Fixtures.Steps.sauce)
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.RECIPES_STEPS_ID, recipeCreated.id, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asStep()
        }

        step.shouldNotBeNull()
        step shouldBeStep Fixtures.Steps.sauce
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a step and throw bad-request with invalid recipe-id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_STEPS_ID, 100, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not delete a step and throw bad-request with invalid id`() {
        createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_STEPS_ID, recipeCreated.id, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a step with valid id`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_STEPS_ID, recipeCreated.id, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    @Test
    fun `should delete a step when deleting a recipe`() {
        val stepCreated = createStep(recipeCreated.id, Fixtures.Steps.dough)

        // delete the recipe
        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // try to read the step
        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_STEPS_ID, recipeCreated.id, stepCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    // endregion
}
