package com.alex.munchies.domain

data class Step(
    val id: Long,
    val userId: String?,
    val recipeId: Long,
    val number: Int,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long
)
