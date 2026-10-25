package org.munchies.entity

import org.munchies.domain.LabelResponse
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class LabelEntity(
    @Id
    val id: Long,
    val userId: String,
    var name: String,
    val createdAt: Long,
    var updatedAt: Long
) {
    fun toDomain() = LabelResponse(id, userId, name, createdAt, updatedAt)
}
