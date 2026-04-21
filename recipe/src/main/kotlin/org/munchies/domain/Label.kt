package org.munchies.domain

import jakarta.validation.constraints.NotBlank

data class LabelRequest(@field:NotBlank val name: String)

data class LabelResponse(
    val id: Long,
    val userId: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
