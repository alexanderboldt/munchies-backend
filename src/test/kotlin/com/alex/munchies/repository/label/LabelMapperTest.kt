package com.alex.munchies.repository.label

import com.alex.munchies.Fixtures
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LabelMapperTest {

    @Test
    fun `should map domain to entity`() {
        val domain = Fixtures.Labels.Domain.vegetarian
        val entity = domain.toEntity(Fixtures.User.userId) // information like id, createdAt and updatedAt will be set automatically

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(Fixtures.User.userId)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should combine a new domain with an existing entity`() {
        val domain = Fixtures.Labels.Domain.vegan
        val entity = Fixtures.Labels.Entity.vegetarian

        val combined = domain + entity

        assertThat(combined.id).isEqualTo(entity.id)
        assertThat(combined.userId).isEqualTo(entity.userId)
        assertThat(combined.name).isEqualTo(domain.name)
        assertThat(combined.createdAt).isEqualTo(entity.createdAt)
        assertThat(combined.updatedAt).isGreaterThan(0)
        assertThat(combined.updatedAt).isNotEqualTo(entity.updatedAt)
    }

    @Test
    fun `should map entity to domain`() {
        val entity = Fixtures.Labels.Entity.vegetarian
        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.userId).isEqualTo(entity.userId)
        assertThat(domain.name).isEqualTo(entity.name)
        assertThat(domain.createdAt).isEqualTo(entity.createdAt)
        assertThat(domain.updatedAt).isEqualTo(entity.updatedAt)
    }
}