package com.alex.munchies.controller

import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.recipe.RecipeRepository
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
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("tests")
class RecipeResourceTest : BaseResourceTest() {

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    private object Recipes {
        val pizza = ApiModelRecipe(0, "", null,"Pizza", "lecker", 1000, 1747138632, 1747138632)
        val burger = ApiModelRecipe(0, "", null, "Burger", "juicy", 2000, 1747138632, 1747138632)
    }

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
            body(Recipes.pizza)
        } When {
            post(Routes.Recipe.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertRecipe(Recipes.pizza)
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
            body(Recipes.pizza)
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
            assertRecipe(Recipes.pizza, true)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postRecipe(Recipes.pizza)

        When {
            get(Routes.Recipe.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postRecipe(Recipes.pizza)

        When {
            get(Routes.Recipe.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertRecipe(Recipes.pizza)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        postRecipe(Recipes.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Recipes.burger)
        } When {
            put(Routes.Recipe.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testUpdateWithValidId() {
        val id = postRecipe(Recipes.pizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Recipes.burger)
        } When {
            put(Routes.Recipe.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertRecipe(Recipes.burger)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        postRecipe(Recipes.pizza)

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
        val id = postRecipe(Recipes.pizza)

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

    fun postRecipe(recipe: ApiModelRecipe): Int {
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

    private fun ValidatableResponse.assertRecipe(recipe: ApiModelRecipe, isInArray: Boolean = false) {
        val suffix = if (isInArray) "[0]" else ""

        body("id".plus(suffix), Matchers.greaterThan(0))
        body("userId".plus(suffix), equalTo(userId))
        body("title".plus(suffix), equalTo(recipe.title))
        body("description".plus(suffix), equalTo(recipe.description))
        body("duration".plus(suffix), equalTo(recipe.duration))
        body("createdAt".plus(suffix), Matchers.greaterThan(0L))
        body("updatedAt".plus(suffix), Matchers.greaterThan(0L))
    }
}