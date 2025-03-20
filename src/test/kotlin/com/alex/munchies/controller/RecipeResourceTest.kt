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

    private val userId = "12345"

    @LocalServerPort
    private var port: Int = 0

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port

        Mockito.`when`(userService.getUserId()).thenReturn(userId)
    }

    @AfterEach
    fun afterEach() {
        recipeRepository.deleteAll()
    }

    @Test
    fun testPostWithValidRequest() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(ApiModelRecipePost("Pizza", "lecker", 1000))
        } When {
            post("/api/v1/recipes")
        } Then {
            statusCode(201)
            body("id", Matchers.greaterThan(0))
            body("userId", equalTo(userId))
            body("title", equalTo("Pizza"))
            body("description", equalTo("lecker"))
            body("duration", equalTo(1000))
            body("createdAt", Matchers.greaterThan(0L))
            body("updatedAt", Matchers.greaterThan(0L))
        }
    }

    @Test
    fun testGetAllWithNoRecipes() {
        When {
            get("/api/v1/recipes")
        } Then {
            statusCode(200)
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneRecipe() {
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(ApiModelRecipePost("Pizza", "lecker", 1000))
        } When {
            post("/api/v1/recipes")
        } Then {
            statusCode(201)
        }

        When {
            get("/api/v1/recipes")
        } Then {
            statusCode(200)
            body("id[0]", Matchers.greaterThan(0))
            body("userId[0]", equalTo(userId))
            body("title[0]", equalTo("Pizza"))
            body("description[0]", equalTo("lecker"))
            body("duration[0]", equalTo(1000))
            body("createdAt[0]", Matchers.greaterThan(0L))
            body("updatedAt[0]", Matchers.greaterThan(0L))
        }
    }

    @Test
    fun testGetOneWithValidRecipe() {
        val id: Int = Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(ApiModelRecipePost("Pizza", "lecker", 1000))
        } When {
            post("/api/v1/recipes")
        } Then {
            statusCode(201)
        } Extract {
            path("id")
        }

        When {
            get("/api/v1/recipes/{id}", id)
        } Then {
            statusCode(200)
            body("id", Matchers.greaterThan(0))
            body("userId", equalTo(userId))
            body("title", equalTo("Pizza"))
            body("description", equalTo("lecker"))
            body("duration", equalTo(1000))
            body("createdAt", Matchers.greaterThan(0L))
            body("updatedAt", Matchers.greaterThan(0L))
        }
    }
}