package org.munchies.domain

data class RecipeRequest(
    val labelId: Long?,
    val title: String,
    val description: String?,
    val duration: Int
)

data class RecipeResponse(
    val id: Long,
    val userId: String,
    val labelId: Long?,
    val title: String,
    val description: String?,
    val duration: Int,
    val filename: String?,
    val createdAt: Long,
    val updatedAt: Long
)
