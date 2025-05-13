package com.alex.munchies.repository.database.recipe

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class DbModelRecipe(
    @Id
    @GeneratedValue
    val id: Long,

    val userId: String,

    val labelId: Long?,

    val title: String,

    val description: String?,

    val duration: Int,

    val createdAt: Long,

    val updatedAt: Long
)
