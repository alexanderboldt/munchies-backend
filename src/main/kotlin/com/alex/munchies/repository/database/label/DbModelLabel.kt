package com.alex.munchies.repository.database.label

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class DbModelLabel(
    @Id
    @GeneratedValue
    val id: Long,

    val userId: String,

    val name: String,

    val createdAt: Long,

    val updatedAt: Long
)