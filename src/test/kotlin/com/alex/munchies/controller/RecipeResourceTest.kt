package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.recipe.RecipeRepository
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
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
    fun testPostWithValidRequest() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.pizza)
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertRecipe(Fixtures.Recipes.Domain.pizza)
        }
    }

    @Test
    fun testPostWithInvalidLabelId() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.pizza.copy(labelId = 100))
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun testPostWithValidLabelId() {
        val labelId: Int = Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Labels.Domain.vegetarian)
        } When {
            post(Routes.Label.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.pizza.copy(labelId = labelId.toLong()))
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertRecipe(Fixtures.Recipes.Domain.pizza)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoRecipes() {
        When {
            get(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneRecipe() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.pizza)
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        }

        When {
            get(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertRecipe(Fixtures.Recipes.Domain.pizza, true)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            get(Routes.Recipe.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        When {
            get(Routes.Recipe.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertRecipe(Fixtures.Recipes.Domain.pizza)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Routes.Recipe.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testUpdateWithValidId() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Recipes.Domain.burger)
        } When {
            put(Routes.Recipe.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertRecipe(Fixtures.Recipes.Domain.burger)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        postRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Recipe.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testDeleteWithValidRecipe() {
        val id = postRecipe(Fixtures.Recipes.Domain.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Recipe.detail, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    fun postRecipe(recipe: Recipe): Int {
        return Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(recipe)
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertRecipe(recipe: Recipe, isInArray: Boolean = false) {
        val suffix = if (isInArray) "[0]" else ""

        body("id".plus(suffix), Matchers.greaterThan(0))
        body("userId".plus(suffix), equalTo(Fixtures.User.userId))
        body("title".plus(suffix), equalTo(recipe.title))
        body("description".plus(suffix), equalTo(recipe.description))
        body("duration".plus(suffix), equalTo(recipe.duration))
        body("createdAt".plus(suffix), Matchers.greaterThan(0L))
        body("updatedAt".plus(suffix), Matchers.greaterThan(0L))
    }
}