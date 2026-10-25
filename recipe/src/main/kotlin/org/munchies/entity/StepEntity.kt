package org.munchies.entity

import org.munchies.domain.StepResponse
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class StepEntity(
    @Id
    val id: Long,
    val userId: String,
    val recipeId: Long,
    var number: Int,
    var title: String,
    var description: String,
    val createdAt: Long,
    var updatedAt: Long
) {
    fun toDomain() = StepResponse(id, userId, recipeId, number, title, description, createdAt, updatedAt)
}
