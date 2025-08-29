package com.alex.munchies.domain

data class Recipe(
    val id: Long,
    val userId: String?,
    val labelId: Long?,
    val title: String,
    val description: String?,
    val duration: Int,
    val filename: String?,
    val createdAt: Long,
    val updatedAt: Long
)
