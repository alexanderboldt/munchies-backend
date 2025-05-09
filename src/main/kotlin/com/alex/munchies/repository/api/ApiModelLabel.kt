package com.alex.munchies.repository.api

data class ApiModelLabel(
    val id: Long,
    val userId: String?,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
