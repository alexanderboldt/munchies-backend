package com.alex.munchies.repository.label

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class LabelEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val userId: String,

    val name: String,

    val createdAt: Long,

    val updatedAt: Long
)
