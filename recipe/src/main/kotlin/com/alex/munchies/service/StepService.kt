package com.alex.munchies.service

import com.alex.munchies.domain.StepRequest
import com.alex.munchies.domain.StepResponse
import com.alex.munchies.entity.StepEntity
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.RecipeRepository
import com.alex.munchies.repository.StepRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.Date

@Service
class StepService(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository
) {

    // create

    fun create(userId: String, recipeId: Long, step: StepRequest): StepResponse {
        // check if the recipe exists
        recipeRepository.existsByIdAndUserIdOrThrow(recipeId, userId)

        // arrange the information
        val entity = StepEntity(
            0,
            userId,
            recipeId,
            step.number,
            step.title,
            step.description,
            Date().time,
            Date().time
        )

        // save the step
        return stepRepository
            .save(entity)
            .toDomain()
    }

    // read

    fun readAll(userId: String, recipeId: Long) = stepRepository
        .findAllByUserIdAndRecipeId(userId, recipeId)
        .map { it.toDomain() }

    fun read(userId: String, id: Long, recipeId: Long) = stepRepository
        .findByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
        .toDomain()

    // update

    @Transactional
    fun update(userId: String, id: Long, recipeId: Long, stepUpdate: StepRequest) = stepRepository
        .findByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
        .apply {
            number = stepUpdate.number
            title = stepUpdate.title
            description = stepUpdate.description
            updatedAt = Date().time
        }.toDomain()

    // delete

    fun delete(userId: String, id: Long, recipeId: Long) {
        stepRepository.apply {
            existsByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
            deleteByIdAndUserId(id, userId)
        }
    }
}
