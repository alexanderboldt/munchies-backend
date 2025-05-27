package com.alex.munchies.repository.recipe

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RecipeMapperTest {

    private val userId = "12345"

    @Test
    fun `should map domain to entity`() {
        val domain = Recipe(-1, null, 10, "Pizza", "Italian Style", 1000,0, 0)
        val entity = domain.toEntity(userId) // information like id, createdAt and updatedAt will be set automatically

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(userId)
        assertThat(entity.labelId).isEqualTo(domain.labelId)
        assertThat(entity.title).isEqualTo(domain.title)
        assertThat(entity.description).isEqualTo(domain.description)
        assertThat(entity.duration).isEqualTo(domain.duration)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should map meal to entity`() {
        val meal = Meal("231", "Spaghetti Bolognese", "Pasta")
        val entity = meal.toEntity(userId)

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(userId)
        assertThat(entity.labelId).isNull()
        assertThat(entity.title).isEqualTo(meal.strMeal)
        assertThat(entity.description).isEqualTo(meal.strCategory)
        assertThat(entity.duration).isEqualTo(0)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should combine a new domain with an existing entity`() {
        val domain = Recipe(-1, null, 11, "Burger", "A juicy one", 500, 0, 0)
        val entity = RecipeEntity(12, userId, 10, "Pizza", "Italian Style", 1000, 16438423489, 162131233)

        val combined = domain + entity

        assertThat(combined.id).isEqualTo(entity.id)
        assertThat(combined.userId).isEqualTo(entity.userId)
        assertThat(combined.labelId).isEqualTo(domain.labelId)
        assertThat(combined.title).isEqualTo(domain.title)
        assertThat(combined.description).isEqualTo(domain.description)
        assertThat(combined.duration).isEqualTo(domain.duration)
        assertThat(combined.createdAt).isEqualTo(entity.createdAt)
        assertThat(combined.updatedAt).isGreaterThan(0)
        assertThat(combined.updatedAt).isNotEqualTo(entity.updatedAt)
    }

    @Test
    fun `should map entity to domain`() {
        val entity = RecipeEntity(12, userId, 10, "Pizza", "Italian Style", 1000, 16438423489, 162131233)
        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.userId).isEqualTo(entity.userId)
        assertThat(domain.labelId).isEqualTo(entity.labelId)
        assertThat(domain.title).isEqualTo(entity.title)
        assertThat(domain.description).isEqualTo(entity.description)
        assertThat(domain.duration).isEqualTo(entity.duration)
        assertThat(domain.createdAt).isEqualTo(entity.createdAt)
        assertThat(domain.updatedAt).isEqualTo(entity.updatedAt)
    }
}