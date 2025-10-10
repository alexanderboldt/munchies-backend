package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.asRecipe
import com.alex.munchies.util.Path
import com.alex.munchies.util.postRecipe
import com.alex.munchies.util.uploadRecipeImage
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecipeImageControllerTest : BaseControllerTest() {

    private lateinit var recipeCreated: RecipeResponse

    @BeforeEach
    fun beforeEach() {
        // precondition to all tests: post a recipe
        recipeCreated = postRecipe(Fixtures.Recipes.pizza)
    }

    // region upload image

    @Test
    fun `should throw bad-request with invalid id`() {
        Given {
            multiPart("image", Fixtures.image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should upload image and return ok with valid id`() {
        val recipe = Given {
            multiPart("image", Fixtures.image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asRecipe()
        }

        recipe.shouldNotBeNull()
        recipe.filename.shouldNotBeNull()
        recipe.filename.shouldNotBeBlank()
    }

    // endregion

    // region download image

    @Test
    fun `should not download an image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        When {
            get(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should download an image and with valid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        val bytes = When {
            get(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            contentType(ContentType.BINARY)
        } Extract {
            asByteArray()
        }

        bytes.shouldNotBeNull()
        bytes.size shouldBeGreaterThan 0
    }

    // endregion

    // region delete image

    @Test
    fun `should not delete an image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        When {
            delete(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not delete an image and with non existing image`() {
        When {
            delete(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an image and with valid id and existing image`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        When {
            delete(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
