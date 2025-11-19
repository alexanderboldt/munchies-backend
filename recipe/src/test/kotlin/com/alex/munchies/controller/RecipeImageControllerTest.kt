package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.Header
import com.alex.munchies.Param
import com.alex.munchies.Path
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.asRecipe
import com.alex.munchies.util.createRecipe
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
        // precondition to all tests: create a recipe
        recipeCreated = createRecipe(Fixtures.Recipes.pizza)
    }

    // region upload image

    @Test
    fun `should not upload a recipe-image and throw bad-request with invalid id`() {
        Given {
            multiPart(Param.IMAGE, Fixtures.image)
            header(Header.USER_ID, Fixtures.User.USER_ID)
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should upload a recipe-image and return ok with valid id`() {
        val recipe = Given {
            multiPart(Param.IMAGE, Fixtures.image)
            header(Header.USER_ID, Fixtures.User.USER_ID)
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
    fun `should not download a recipe-image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            get(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should download a recipe-image with valid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        val bytes = Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
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
    fun `should not delete a recipe-image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPE_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not delete a recipe-image with non existing image`() {
        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete a recipe-image with valid id and existing image`() {
        // precondition: upload an image
        uploadRecipeImage(recipeCreated.id)

        Given {
            header(Header.USER_ID, Fixtures.User.USER_ID)
        } When {
            delete(Path.RECIPE_IMAGE, recipeCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
