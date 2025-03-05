package com.alex.munchies.controller

import com.alex.munchies.repository.TheMealDbClient
import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelRecipeGet
import com.alex.munchies.repository.api.ApiModelRecipePost
import com.alex.munchies.repository.api.ApiModelTheMealDbPost
import com.alex.munchies.repository.database.RecipeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class RecipeControllerTest {

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
            mysqlContainer.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mysqlContainer.stop()
        }
    }

    private val userId = "12345"

    @MockitoBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var theMealDbClient: TheMealDbClient

    private lateinit var controller: RecipeController

    @BeforeEach
    fun beforeEach() {
        Mockito.`when`(userService.getUserId()).thenReturn(userId)

        controller = RecipeController(userService, recipeRepository, theMealDbClient)
    }

    @AfterEach
    fun afterEach() {
        recipeRepository.deleteAll()
    }

    @Test
    fun testPostRecipe() {
        val recipePost = ApiModelRecipePost("Pizza", "lecker", 1000)

        val recipePosted = controller.postRecipe(recipePost)

        assertRecipe(recipePosted, recipePost)
    }

    @Test
    fun testPostRecipeFromTheMealDb() {
        val recipePost = ApiModelTheMealDbPost("53014")

        val recipePosted = controller.postRecipeFromTheMealDb(recipePost)

        assertThat(recipePosted).isNotNull
        assertThat(recipePosted.id).isNotZero
        assertThat(recipePosted.userId).isEqualTo(userId)
        assertThat(recipePosted.title).isNotBlank
        assertThat(recipePosted.description).isNotBlank
        assertThat(recipePosted.duration).isZero
        assertThat(recipePosted.createdAt).isNotZero
        assertThat(recipePosted.updatedAt).isNotZero
    }

    @Test
    fun testGetOneRecipe() {
        val recipePost = ApiModelRecipePost("Pizza", "lecker", 1000)

        val recipePosted = controller.postRecipe(recipePost)
        val recipeGet = controller.getRecipe(recipePosted.id)

        assertRecipe(recipeGet, recipePost)
    }

    @Test
    fun testGetAllRecipes() {
        val recipePost = ApiModelRecipePost("Pizza", "lecker", 1000)

        controller.postRecipe(recipePost)
        val recipeGet = controller.getAllRecipes()

        assertRecipe(recipeGet[0], recipePost)
    }

    @Test
    fun testUpdateRecipe() {
        val recipePost = ApiModelRecipePost("Pizza", "lecker", 1000)

        val recipePosted = controller.postRecipe(recipePost)

        val recipeUpdate = ApiModelRecipePost("Burger", "lecker", 1000)
        val recipeGet = controller.updateRecipe(recipePosted.id, recipeUpdate)

        assertRecipe(recipeGet, recipeUpdate)
    }

    @Test
    fun testDeleteRecipe() {
        val recipePost = ApiModelRecipePost("Pizza", "lecker", 1000)

        val recipePosted = controller.postRecipe(recipePost)

        controller.deleteRecipe(recipePosted.id)
        val recipesGet = controller.getAllRecipes()

        assertThat(recipesGet).isEmpty()
    }

    fun assertRecipe(actual: ApiModelRecipeGet, expected: ApiModelRecipePost) {
        assertThat(actual).isNotNull
        assertThat(actual.id).isNotZero
        assertThat(actual.userId).isEqualTo(userId)
        assertThat(actual.title).isEqualTo(expected.title)
        assertThat(actual.description).isEqualTo(expected.description)
        assertThat(actual.duration).isEqualTo(expected.duration)
        assertThat(actual.createdAt).isNotZero
        assertThat(actual.updatedAt).isNotZero
    }
}