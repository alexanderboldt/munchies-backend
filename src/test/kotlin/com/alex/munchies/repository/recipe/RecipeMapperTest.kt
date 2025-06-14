package com.alex.munchies.repository.recipe

import com.alex.munchies.Fixtures
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RecipeMapperTest {

    @Test
    fun `should map domain to entity`() {
        val domain = Fixtures.Recipes.Domain.pizza
        val entity = domain.toEntity(Fixtures.User.USER_ID) // information like id, createdAt and updatedAt will be set automatically

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(Fixtures.User.USER_ID)
        assertThat(entity.labelId).isEqualTo(domain.labelId)
        assertThat(entity.title).isEqualTo(domain.title)
        assertThat(entity.description).isEqualTo(domain.description)
        assertThat(entity.duration).isEqualTo(domain.duration)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should map meal to entity`() {
        val meal = Fixtures.Recipes.Domain.meal
        val entity = meal.toEntity(Fixtures.User.USER_ID)

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(Fixtures.User.USER_ID)
        assertThat(entity.labelId).isNull()
        assertThat(entity.title).isEqualTo(meal.strMeal)
        assertThat(entity.description).isEqualTo(meal.strCategory)
        assertThat(entity.duration).isEqualTo(0)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should combine a new domain with an existing entity`() {
        val domain = Fixtures.Recipes.Domain.burger
        val entity = Fixtures.Recipes.Entity.pizza

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
        val entity = Fixtures.Recipes.Entity.pizza
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
