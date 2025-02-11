package com.alex.munchies.repository.database

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class DbModelRecipe(
    @Id
    @GeneratedValue
    val id: Long,

    val userId: String,

    val title: String,

    val description: String?,

    val createdAt: Long,

    val updatedAt: Long
)
