package com.alex.munchies.repository.api

data class ApiModelRecipeGet(
    val id: Long,
    val userId: String,
    val title: String,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long
)