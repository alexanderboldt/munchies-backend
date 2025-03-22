package com.alex.munchies.controller

import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelRecipePost
import com.alex.munchies.repository.database.RecipeRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
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
import org.springframework.http.HttpStatus
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
            registry.add("spring.datasource.username", mysqlContainer::getUsername);
            registry.add("spring.datasource.password", mysqlContainer::getPassword);
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
    
    private val route = "/api/v1/recipes"
    private val routeDetail = "$route/{id}"

    private val recipePizza = ApiModelRecipePost("Pizza", "lecker", 1000)
    private val recipeBurger = ApiModelRecipePost("Burger", "juicy", 2000)

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port

        Mockito.`when`(userService.getUserId()).thenReturn(userId)
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
            body(recipePizza)
        } When {
            post(route)
        } Then {
            statusCode(HttpStatus.CREATED.value())
            assertRecipe(recipePizza)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoRecipes() {
        When {
            get(route)
        } Then {
            statusCode(HttpStatus.OK.value())
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneRecipe() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(recipePizza)
        } When {
            post(route)
        } Then {
            statusCode(HttpStatus.CREATED.value())
        }

        When {
            get(route)
        } Then {
            statusCode(HttpStatus.OK.value())
            body("size()", equalTo(1))
            assertRecipeInArray(recipePizza)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        postRecipe(recipePizza)

        When {
            get(routeDetail, 100)
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Test
    fun testGetOneWithValidId() {
        val id = postRecipe(recipePizza)

        When {
            get(routeDetail, id)
        } Then {
            statusCode(HttpStatus.OK.value())
            assertRecipe(recipePizza)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        postRecipe(recipePizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(recipeBurger)
        } When {
            put(routeDetail, 100)
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Test
    fun testUpdateWithValidId() {
        val id = postRecipe(recipePizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(recipeBurger)
        } When {
            put(routeDetail, id)
        } Then {
            statusCode(HttpStatus.OK.value())
            assertRecipe(recipeBurger)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        postRecipe(recipePizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(routeDetail, 100)
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Test
    fun testDeleteWithValidRecipe() {
        val id = postRecipe(recipePizza)

        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(routeDetail, id)
        } Then {
            statusCode(HttpStatus.NO_CONTENT.value())
        }
    }

    // endregion

    fun postRecipe(recipe: ApiModelRecipePost): Int {
        return Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(recipe)
        } When {
            post(route)
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertRecipe(recipe: ApiModelRecipePost) {
        body("id", Matchers.greaterThan(0))
        body("userId", equalTo(userId))
        body("title", equalTo(recipe.title))
        body("description", equalTo(recipe.description))
        body("duration", equalTo(recipe.duration))
        body("createdAt", Matchers.greaterThan(0L))
        body("updatedAt", Matchers.greaterThan(0L))
    }

    private fun ValidatableResponse.assertRecipeInArray(recipe: ApiModelRecipePost) {
        body("id[0]", Matchers.greaterThan(0))
        body("userId[0]", equalTo(userId))
        body("title[0]", equalTo(recipe.title))
        body("description[0]", equalTo(recipe.description))
        body("duration[0]", equalTo(recipe.duration))
        body("createdAt[0]", Matchers.greaterThan(0L))
        body("updatedAt[0]", Matchers.greaterThan(0L))
    }
}