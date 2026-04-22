package org.munchies.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class RecipeRequest(
    val labelId: Long?,
    @field:NotBlank val title: String,
    val description: String?,
    @field:Positive val duration: Int
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
