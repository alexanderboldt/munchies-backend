package com.alex.munchies.controller

import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.recipe.RecipeRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RecipeResourceTest {

    companion object {
        val mysqlContainer = MySQLContainer("mysql:9.2.0")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create" }
        }

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            RestAssured.baseURI = "http://localhost"

            mysqlContainer.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mysqlContainer.stop()
        }
    }

    @LocalServerPort
    private var port: Int = 0

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    private val userId = "12345"

    private object Routes {
        val main = "/api/v1/recipes"
        val detail = "$main/{id}"
    }

    private object Recipes {
        val pizza = ApiModelRecipe(0, "", "Pizza", "lecker", 1000, 0, 0)
        val burger = ApiModelRecipe(0, "", "Burger", "juicy", 2000, 0, 0)
    }

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port

        Mockito.`when`(userService.userId).thenReturn(userId)
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
            post(Routes.main)
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
            get(Routes.main)
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
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        }

        When {
            get(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertRecipeInArray(Recipes.pizza)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postRecipe(Recipes.pizza)

        When {
            get(Routes.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postRecipe(Recipes.pizza)

        When {
            get(Routes.detail, id)
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
            put(Routes.detail, 100)
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
            put(Routes.detail, id)
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
            delete(Routes.detail, 100)
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
            delete(Routes.detail, id)
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
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertRecipe(recipe: ApiModelRecipe) {
        body("id", Matchers.greaterThan(0))
        body("userId", equalTo(userId))
        body("title", equalTo(recipe.title))
        body("description", equalTo(recipe.description))
        body("duration", equalTo(recipe.duration))
        body("createdAt", Matchers.greaterThan(0L))
        body("updatedAt", Matchers.greaterThan(0L))
    }

    private fun ValidatableResponse.assertRecipeInArray(recipe: ApiModelRecipe) {
        body("id[0]", Matchers.greaterThan(0))
        body("userId[0]", equalTo(userId))
        body("title[0]", equalTo(recipe.title))
        body("description[0]", equalTo(recipe.description))
        body("duration[0]", equalTo(recipe.duration))
        body("createdAt[0]", Matchers.greaterThan(0L))
        body("updatedAt[0]", Matchers.greaterThan(0L))
    }
}