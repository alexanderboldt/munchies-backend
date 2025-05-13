package com.alex.munchies.repository.api

data class ApiModelRecipe(
    val id: Long,
    val userId: String?,
    val labelId: Long?,
    val title: String,
    val description: String?,
    val duration: Int,
    val createdAt: Long,
    val updatedAt: Long
)
