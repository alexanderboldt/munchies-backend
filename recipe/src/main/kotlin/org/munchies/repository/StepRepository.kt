package org.munchies.repository

import kotlinx.coroutines.flow.Flow
import org.munchies.entity.StepEntity
import org.munchies.util.BadRequestException
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface StepRepository : CoroutineCrudRepository<StepEntity, Long>, BaseRepository<StepEntity> {

    fun existsByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): Boolean
    fun existsByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long) {
        if (!existsByIdAndUserIdAndRecipeId(id, userId, recipeId)) throw BadRequestException()
    }

    fun findAllByUserIdAndRecipeId(userId: String, recipeId: Long): Flow<StepEntity>

    fun findByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): StepEntity?
    fun findByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long): StepEntity {
        return findByIdAndUserIdAndRecipeId(id, userId, recipeId) ?: throw BadRequestException()
    }
}
