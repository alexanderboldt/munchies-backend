package com.alex.munchies.mapper

import com.alex.munchies.Fixtures
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class RecipeMapperTest : StringSpec({

    "should map domain to entity" {
        val domain = Fixtures.Recipes.Domain.pizza
        val entity = domain.toEntity(Fixtures.User.USER_ID)

        entity.id shouldBe 0
        entity.userId shouldBe Fixtures.User.USER_ID
        entity.labelId shouldBe domain.labelId
        entity.title shouldBe domain.title
        entity.description shouldBe domain.description
        entity.duration shouldBe domain.duration
        entity.createdAt shouldBeGreaterThan 0
        entity.updatedAt shouldBeGreaterThan 0
    }

    "should map meal to entity" {
        val meal = Fixtures.Recipes.Domain.meal
        val entity = meal.toEntity(Fixtures.User.USER_ID)

        entity.id shouldBe 0
        entity.userId shouldBe Fixtures.User.USER_ID
        entity.labelId shouldBe null
        entity.title shouldBe meal.strMeal
        entity.description shouldBe meal.strCategory
        entity.duration shouldBe 0
        entity.createdAt shouldBeGreaterThan 0
        entity.updatedAt shouldBeGreaterThan 0
    }

    "should combine a new domain with an existing entity" {
        val domain = Fixtures.Recipes.Domain.burger
        val entity = Fixtures.Recipes.Entity.pizza

        val combined = domain + entity

        combined.id shouldBe entity.id
        combined.userId shouldBe entity.userId
        combined.labelId shouldBe domain.labelId
        combined.title shouldBe domain.title
        combined.description shouldBe domain.description
        combined.duration shouldBe domain.duration
        combined.createdAt shouldBe entity.createdAt
        combined.updatedAt shouldBeGreaterThan 0
        combined.updatedAt shouldNotBe entity.updatedAt
    }

    "should map entity to domain" {
        val entity = Fixtures.Recipes.Entity.pizza
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.userId shouldBe entity.userId
        domain.labelId shouldBe entity.labelId
        domain.title shouldBe entity.title
        domain.description shouldBe entity.description
        domain.duration shouldBe entity.duration
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
