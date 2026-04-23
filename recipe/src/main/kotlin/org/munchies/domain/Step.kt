package org.munchies.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class StepRequest(
    @field:Positive val number: Int,
    @field:NotBlank val title: String,
    @field:NotBlank val description: String
)

data class StepResponse(
    val id: Long,
    val userId: String,
    val recipeId: Long,
    val number: Int,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long
)
