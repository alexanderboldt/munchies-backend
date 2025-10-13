package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.Path
import com.alex.munchies.util.postRecipe
import com.alex.munchies.util.postStep
import com.alex.munchies.util.shouldBeStep
import io.kotest.matchers.nulls.shouldNotBeNull
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

}
