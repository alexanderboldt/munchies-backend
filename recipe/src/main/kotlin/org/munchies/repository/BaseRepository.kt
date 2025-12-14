package org.munchies.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * Interface contains base functionalities for all repositories.
 *
 * @param T The type of the entity managed by this repository.
 */
interface BaseRepository<T> {

    // read

    suspend fun existsByIdAndUserId(id: Long, userId: String): Boolean

    fun findAllByUserId(userId: String, sort: Sort): Flow<T>
    fun findAllByUserId(userId: String, page: Pageable): Flow<T>

    suspend fun findByIdAndUserId(id: Long, userId: String): T?

    // delete

    suspend fun deleteByIdAndUserId(id: Long, userId: String)
}
