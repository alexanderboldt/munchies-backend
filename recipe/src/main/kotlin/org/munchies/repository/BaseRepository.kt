package org.munchies.repository

import org.munchies.util.BadRequestException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * Interface contains base functionalities for all repositories.
 *
 * @param T The type of the entity managed by this repository.
 */
interface BaseRepository<T> {

    // read

    fun existsByIdAndUserId(id: Long, userId: String): Boolean
    fun existsByIdAndUserIdOrThrow(id: Long, userId: String) {
        if (!existsByIdAndUserId(id, userId)) throw BadRequestException()
    }

    fun findAllByUserId(userId: String, sort: Sort): List<T>
    fun findAllByUserId(userId: String, page: Pageable): List<T>

    fun findByIdAndUserId(id: Long, userId: String): T?
    fun findByIdAndUserIdOrThrow(id: Long, userId: String) = findByIdAndUserId(id, userId) ?: throw BadRequestException()

    // delete

    @Transactional
    fun deleteByIdAndUserId(id: Long, userId: String)
}
