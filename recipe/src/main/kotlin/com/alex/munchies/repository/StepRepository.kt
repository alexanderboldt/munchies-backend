package com.alex.munchies.repository

import com.alex.munchies.entity.StepEntity
import com.alex.munchies.util.BadRequestException
import org.springframework.data.repository.CrudRepository

interface StepRepository : CrudRepository<StepEntity, Long>, BaseRepository<StepEntity> {

    fun existsByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): Boolean
    fun existsByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long) {
        if (!existsByIdAndUserIdAndRecipeId(id, userId, recipeId)) throw BadRequestException()
    }

    fun findAllByUserIdAndRecipeId(userId: String, recipeId: Long): List<StepEntity>

    fun findByIdAndUserIdAndRecipeId(id: Long, userId: String, recipeId: Long): StepEntity?
    fun findByIdAndUserIdAndRecipeIdOrThrow(id: Long, userId: String, recipeId: Long): StepEntity {
        return findByIdAndUserIdAndRecipeId(id, userId, recipeId) ?: throw BadRequestException()
    }
}
