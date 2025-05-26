package com.alex.munchies.repository

/**
 * Interface contains base functionalities for all repositories.
 *
 * @param T The type of the entity managed by this repository.
 */
interface Repository<T> {

    fun existsByIdAndUserId(id: Long, userId: String): Boolean

    fun findAllByUserId(userId: String): List<T>

    fun findByIdAndUserId(id: Long, userId: String): T?
}