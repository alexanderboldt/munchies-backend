package org.munchies.controller

import org.munchies.Fixtures
import org.munchies.Header
import org.munchies.Path
import org.munchies.util.asRecipe
import org.munchies.util.asRecipes
import org.munchies.util.createLabel
import org.munchies.util.createRecipe
import org.munchies.util.shouldBeRecipe
import org.munchies.util.shouldBeRecipes
import org.munchies.util.uploadRecipeImage
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class RecipeControllerTest : BaseControllerTest() {

    // region create

    @Test
    fun `should create a recipe with valid request`() {
        val recipe = createRecipe(Fixtures.Recipes.pizza)

        recipe.shouldNotBeNull()
        recipe shouldBeRecipe Fixtures.Recipes.pizza
    }

    @Test
    fun `should not create a recipe with invalid label-id`() {
        Given {
            body(Fixtures.Recipes.pizza.copy(labelId = 100))
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            post(Path.RECIPES)
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun `should create a recipe with valid label-id`() {
        val labelCreated = createLabel(Fixtures.Labels.vegetarian)

        val recipeRequest = Fixtures.Recipes.pizza.copy(labelId = labelCreated.id)

        val recipeCreated = createRecipe(recipeRequest)

        recipeCreated shouldBeRecipe recipeRequest
    }

    // endregion

    // region read all

    @Test
    fun `should read all recipes and return an empty list`() {
        val recipes = Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipes()
        }

        recipes.shouldNotBeNull()
        recipes shouldBe emptyList()
    }


    @Test
    fun `should read all recipes and return a list with one recipe`() {
        createRecipe(Fixtures.Recipes.pizza)

        val recipes = Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipes()
        }

        recipes shouldHaveSize 1
        recipes shouldBeRecipes listOf(Fixtures.Recipes.pizza)
    }

    @Test
    fun `should read all recipes and return a list with ten recipes`() {
        val recipesRequest = (1..10).map { Fixtures.Recipes.pizza }

        recipesRequest.forEach { createRecipe(it) }

        val recipes = Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES)
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
    fun `should not read one recipe and throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.pizza)

        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should read one recipe and return it with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.pizza)

        val recipe = Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe shouldBeRecipe Fixtures.Recipes.pizza
    }

    // endregion

    // region update

    @Test
    fun `should not update a recipe and throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.pizza)

        Given {
            body(Fixtures.Recipes.burger)
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.RECIPES_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update a recipe and return it with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.pizza)

        val recipe = Given {
            body(Fixtures.Recipes.burger)
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            put(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe shouldBeRecipe Fixtures.Recipes.burger
    }

    @Test
    fun `should update label-id to null when deleting a label`() {
        // precondition: create a label and connect it to a created recipe
        val labelCreated = createLabel(Fixtures.Labels.vegetarian)

        val recipeRequest = Fixtures.Recipes.pizza.copy(labelId = labelCreated.id)

        val recipeCreated = createRecipe(recipeRequest)

        // execute: delete the label
        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.LABELS_ID, labelCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // verify: the label-id of a recipe is null
        val recipe = Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe shouldBeRecipe Fixtures.Recipes.pizza
    }

    // endregion

    // region delete

    @Test
    fun `should not delete a recipe and throw bad-request with invalid id`() {
        createRecipe(Fixtures.Recipes.pizza)

        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a recipe with valid id`() {
        val recipeCreated = createRecipe(Fixtures.Recipes.pizza)

        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    @Test
    suspend fun `should delete a recipe and an image with valid id`() {
        val recipeCreated = uploadRecipeImage(createRecipe(Fixtures.Recipes.pizza).id)

        // execute the delete and verify
        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPES_ID, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // try to download the image and verify, that it is deleted
        Given {
            header(Header.API_VERSION, "1")
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPES_IMAGES, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    // endregion
}
