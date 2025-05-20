package com.alex.munchies.domain

data class Label(
    val id: Long,
    val userId: String?,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
