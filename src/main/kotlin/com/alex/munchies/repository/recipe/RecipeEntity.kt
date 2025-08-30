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

    var labelId: Long?,

    var title: String,

    var description: String?,

    var duration: Int,

    val filename: String?,

    val createdAt: Long,

    var updatedAt: Long
)
