package com.alex.munchies.repository.database.label

import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<DbModelLabel, Long> {

    fun existsByIdAndUserId(id: Long, userId: String): Boolean

    fun findAllByUserId(userId: String): List<DbModelLabel>

    fun findByIdAndUserId(id: Long, userId: String): DbModelLabel?
}