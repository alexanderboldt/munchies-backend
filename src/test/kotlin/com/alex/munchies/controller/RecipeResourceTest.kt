package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.Recipe
import com.alex.munchies.extension.asRecipe
import com.alex.munchies.extension.asRecipes
import com.alex.munchies.repository.recipe.RecipeRepository
import com.alex.munchies.util.Path
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RecipeResourceTest : BaseResourceTest() {

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @AfterEach
    fun afterEach() {
        recipeRepository.deleteAll()
    }

    // region create

    @Test
    fun `should create a recipe with valid request`() {
        val recipe = createRecipe(Fixtures.Recipes.Domain.pizza)

        recipe.shouldNotBeNull()
        recipe shouldBeRecipe Fixtures.Recipes.Domain.pizza
    }

    @Test
    fun `should not create a recipe with invalid label-id`() {
        Given {
            body(Fixtures.Recipes.Domain.pizza.copy(labelId = 100))
        } When {
            post(Path.RECIPE)
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun `should create a recipe with valid label-id`() {
        val labelCreated = createLabel(Fixtures.Labels.Domain.vegetarian)

        val recipeRequest = Fixtures.Recipes.Domain.pizza.copy(labelId = labelCreated.id)

        val recipeCreated = createRecipe(recipeRequest)

        recipeCreated shouldBeRecipe recipeRequest
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val recipes = When {
            get(Path.RECIPE)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipes()
        }

        recipes.shouldNotBeNull()
        recipes shouldBe emptyList()
    }


    @Test
    fun `should return a list with one recipe`() {
        createRecipe(Fixtures.Recipes.Domain.pizza)

        val recipes = When {
            get(Path.RECIPE)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipes()
        }

        recipes shouldHaveSize 1
        recipes shouldBeRecipes listOf(Fixtures.Recipes.Domain.pizza)
    }

    @Test
    fun `should return a list with ten recipes`() {
        val recipesRequest = (1..10).map { Fixtures.Recipes.Domain.pizza }

        recipesRequest.forEach { createRecipe(it) }

        val recipes = When {
            get(Path.RECIPE)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipes()
        }

        recipes shouldHaveSize 10
        recipes shouldBeRecipes recipesRequest
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            get(Path.RECIPE_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one recipe with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.Domain.pizza)

        val recipe = When {
            get(Path.RECIPE_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe shouldBeRecipe Fixtures.Recipes.Domain.pizza
    }

    // endregion

    // region update

    @Test
    fun `should not update a recipe and throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Path.RECIPE_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return a recipe with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.Domain.pizza)

        val recipe = Given {
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Path.RECIPE_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe shouldBeRecipe Fixtures.Recipes.Domain.burger
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a recipe and throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            delete(Path.RECIPE_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a recipe with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            delete(Path.RECIPE_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
    
    private infix fun List<Recipe>.shouldBeRecipes(expected: List<Recipe>) {
        zip(expected).forEach { (recipeActual, recipeExpected) ->
            recipeActual shouldBeRecipe recipeExpected
        }
    }
    
    private infix fun Recipe.shouldBeRecipe(expected: Recipe) {
        id shouldBeGreaterThan 0
        userId shouldBe Fixtures.User.USER_ID
        title shouldBe expected.title
        description shouldBe expected.description
        duration shouldBe expected.duration
        createdAt shouldBeGreaterThan 0
        updatedAt shouldBeGreaterThan 0
    }
}
