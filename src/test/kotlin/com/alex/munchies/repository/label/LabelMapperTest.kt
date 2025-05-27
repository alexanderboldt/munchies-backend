package com.alex.munchies.repository.label

import com.alex.munchies.domain.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LabelMapperTest {

    private val userId = "12345"

    @Test
    fun `should map domain to entity`() {
        val domain = Label(-1, null, "Vegetarian", 0, 0)
        val entity = domain.toEntity(userId) // information like id, createdAt and updatedAt will be set automatically

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.userId).isEqualTo(userId)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.createdAt).isGreaterThan(0)
        assertThat(entity.updatedAt).isGreaterThan(0)
    }

    @Test
    fun `should combine a new domain with an existing entity`() {
        val domain = Label(-1, null, "Vegan", 0, 0)
        val entity = LabelEntity(12, userId, "Vegetarian", 16438423489, 162131233)

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
        val entity = LabelEntity(12, userId, "Vegetarian", 23784434, 9789683)
        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.userId).isEqualTo(entity.userId)
        assertThat(domain.name).isEqualTo(entity.name)
        assertThat(domain.createdAt).isEqualTo(entity.createdAt)
        assertThat(domain.updatedAt).isEqualTo(entity.updatedAt)
    }
}