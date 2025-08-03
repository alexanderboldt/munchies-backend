package com.alex.munchies.mapper

import com.alex.munchies.Fixtures
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class LabelMapperTest : StringSpec({

    "should map domain to entity" {
        val domain = Fixtures.Labels.Domain.vegetarian
        val entity = domain.toEntity(Fixtures.User.USER_ID) // information like id, createdAt and updatedAt will be set automatically

        entity.id shouldBe 0
        entity.userId shouldBe Fixtures.User.USER_ID
        entity.name shouldBe domain.name
        entity.createdAt shouldBeGreaterThan 0
        entity.updatedAt shouldBeGreaterThan 0
    }

    "should combine a new domain with an existing entity" {
        val domain = Fixtures.Labels.Domain.vegan
        val entity = Fixtures.Labels.Entity.vegetarian

        val combined = domain + entity

        combined.id shouldBe entity.id
        combined.userId shouldBe entity.userId
        combined.name shouldBe domain.name
        combined.createdAt shouldBe entity.createdAt
        combined.updatedAt shouldBeGreaterThan 0
        combined.updatedAt shouldNotBe entity.updatedAt
    }

    "should map entity to domain" {
        val entity = Fixtures.Labels.Entity.vegetarian
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.userId shouldBe entity.userId
        domain.name shouldBe entity.name
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
