package org.munchies.repository

import kotlinx.coroutines.flow.Flow
import org.munchies.util.BadRequestException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

/**
 * Interface contains base functionalities for all repositories.
 *
 * @param T The type of the entity managed by this repository.
 */
interface BaseRepository<T> {

    // read

    suspend fun existsByIdAndUserId(id: Long, userId: String): Boolean
    suspend fun existsByIdAndUserIdOrThrow(id: Long, userId: String) {
        if (!existsByIdAndUserId(id, userId)) throw BadRequestException()
    }

    fun findAllByUserId(userId: String, sort: Sort): Flow<T>
    fun findAllByUserId(userId: String, page: Pageable): Flow<T>

    suspend fun findByIdAndUserId(id: Long, userId: String): T?
    suspend fun findByIdAndUserIdOrThrow(id: Long, userId: String) = findByIdAndUserId(id, userId) ?: throw BadRequestException()

    // delete

    @Transactional
    suspend fun deleteByIdAndUserId(id: Long, userId: String)
}
