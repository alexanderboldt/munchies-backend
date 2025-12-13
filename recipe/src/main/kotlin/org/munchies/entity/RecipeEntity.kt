package org.munchies.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class RecipeEntity(
    @Id
    val id: Long,
    val userId: String,
    var labelId: Long?,
    var title: String,
    var description: String?,
    var duration: Int,
    var filename: String?,
    val createdAt: Long,
    var updatedAt: Long
)
