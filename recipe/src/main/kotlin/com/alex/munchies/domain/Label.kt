package com.alex.munchies.domain

data class LabelRequest(val name: String)

data class LabelResponse(
    val id: Long,
    val userId: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
