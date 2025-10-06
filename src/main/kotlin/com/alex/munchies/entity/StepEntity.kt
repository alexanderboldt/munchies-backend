package com.alex.munchies.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class StepEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val userId: String,
    val recipeId: Long,
    var number: Int,
    var title: String,
    var description: String,
    val createdAt: Long,
    var updatedAt: Long
)
