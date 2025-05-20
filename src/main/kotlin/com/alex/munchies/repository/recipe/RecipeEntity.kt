package com.alex.munchies.repository.recipe

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class RecipeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val userId: String,

    val labelId: Long?,

    val title: String,

    val description: String?,

    val duration: Int,

    val createdAt: Long,

    val updatedAt: Long
)
