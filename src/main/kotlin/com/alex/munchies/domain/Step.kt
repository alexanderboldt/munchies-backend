package com.alex.munchies.domain

data class StepRequest(
    val number: Int,
    val title: String,
    val description: String
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
