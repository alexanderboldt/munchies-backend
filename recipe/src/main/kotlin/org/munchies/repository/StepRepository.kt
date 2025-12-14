package org.munchies.repository

import kotlinx.coroutines.flow.Flow
import org.munchies.entity.StepEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface StepRepository : CoroutineCrudRepository<StepEntity, Long>, BaseRepository<StepEntity> {

    suspend fun existsByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): Boolean

    fun findAllByUserIdAndRecipeId(userId: String, recipeId: Long): Flow<StepEntity>

    suspend fun findByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): StepEntity?
}
