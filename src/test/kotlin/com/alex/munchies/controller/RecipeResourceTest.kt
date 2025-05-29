package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.recipe.RecipeRepository
import io.restassured.common.mapper.TypeRef
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ResponseBodyExtractionOptions
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
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
        val recipe = Given {
            body(Fixtures.Recipes.Domain.pizza)
        } When {
            post(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asRecipe()
        }

        assertThat(recipe).isNotNull
        assertRecipe(recipe, Fixtures.Recipes.Domain.pizza)
    }

    @Test
    fun `should not create a recipe with invalid label-id`() {
        Given {
            body(Fixtures.Recipes.Domain.pizza.copy(labelId = 100))
        } When {
            post(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun `should create a recipe with valid label-id`() {
        val labelId: Int = Given {
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }

        val recipeRequest = Fixtures.Recipes.Domain.pizza.copy(labelId = labelId.toLong())

        val recipeResponse = Given {
            body(recipeRequest)
        } When {
            post(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asRecipe()
        }

        assertRecipe(recipeResponse, recipeRequest)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val recipes = When {
            get(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        } Extract {
            asRecipes()
        }

        assertThat(recipes).isNotNull
        assertThat(recipes).isEmpty()
    }


    @Test
    fun `should return a list with one recipe`() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        val recipes = When {
            get(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
        } Extract {
            asRecipes()
        }

        assertThat(recipes).isNotEmpty
        assertThat(recipes).hasSize(1)
        assertRecipes(recipes, listOf(Fixtures.Recipes.Domain.pizza))
    }

    @Test
    fun `should return a list with ten recipes`() {
        val recipesRequest = (1..10).map { Fixtures.Recipes.Domain.pizza }

        recipesRequest.forEach { postRecipe(it) }

        val recipes = When {
            get(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(10))
        } Extract {
            asRecipes()
        }

        assertThat(recipes).isNotEmpty
        assertThat(recipes).hasSize(10)
        assertRecipes(recipes, recipesRequest)
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            get(Routes.Recipe.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one recipe with valid id`() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        val recipe = When {
            get(Routes.Recipe.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        assertRecipe(recipe, Fixtures.Recipes.Domain.pizza)
    }

    // endregion

    // region update

    @Test
    fun `should not update a recipe and throw bad-request with invalid id`() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Routes.Recipe.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return a recipe with valid id`() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        val recipe = Given {
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Routes.Recipe.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        assertRecipe(recipe, Fixtures.Recipes.Domain.burger)
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a recipe and throw bad-request with invalid id`() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            delete(Routes.Recipe.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a recipe with valid id`() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            delete(Routes.Recipe.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    fun postRecipe(recipe: Recipe): Int {
        return Given {
            body(recipe)
        } When {
            post(Routes.Recipe.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ResponseBodyExtractionOptions.asRecipes() = `as`(object : TypeRef<List<Recipe>>() {})
    private fun ResponseBodyExtractionOptions.asRecipe() = `as`(object : TypeRef<Recipe>() {})

    private fun assertRecipes(recipesActual: List<Recipe>, recipesOther: List<Recipe>) {
        recipesActual.zip(recipesOther).forEach { (recipeActual, recipeOther) ->
            assertRecipe(recipeActual, recipeOther)
        }
    }

    private fun assertRecipe(recipeActual: Recipe, recipeOther: Recipe) {
        assertThat(recipeActual.id).isGreaterThan(0)
        assertThat(recipeActual.userId).isEqualTo(Fixtures.User.USER_ID)
        assertThat(recipeActual.title).isEqualTo(recipeOther.title)
        assertThat(recipeActual.description).isEqualTo(recipeOther.description)
        assertThat(recipeActual.duration).isEqualTo(recipeOther.duration)
        assertThat(recipeActual.createdAt).isGreaterThan(0)
        assertThat(recipeActual.updatedAt).isGreaterThan(0)
    }
}