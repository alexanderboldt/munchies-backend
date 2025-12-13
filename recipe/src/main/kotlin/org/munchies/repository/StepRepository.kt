package org.munchies.repository

import kotlinx.coroutines.flow.Flow
import org.munchies.entity.StepEntity
import org.munchies.util.BadRequestException
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface StepRepository : CoroutineCrudRepository<StepEntity, Long>, BaseRepository<StepEntity> {

    suspend fun existsByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): Boolean
    suspend fun existsByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long) {
        if (!existsByIdAndUserIdAndRecipeId(id, userId, recipeId)) throw BadRequestException()
    }

    fun findAllByUserIdAndRecipeId(userId: String, recipeId: Long): Flow<StepEntity>

    suspend fun findByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): StepEntity?
    suspend fun findByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long): StepEntity {
        return findByIdAndUserIdAndRecipeId(id, userId, recipeId) ?: throw BadRequestException()
    }
}
