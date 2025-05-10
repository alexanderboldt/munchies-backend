package com.alex.munchies.repository.database

/**
 * Interface contains base functionalities for all repositories.
 */
interface Repository<T> {

    fun existsByIdAndUserId(id: Long, userId: String): Boolean

    fun findAllByUserId(userId: String): List<T>

    fun findByIdAndUserId(id: Long, userId: String): T?
}