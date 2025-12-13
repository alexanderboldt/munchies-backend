package org.munchies.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.munchies.domain.StepRequest
import org.munchies.domain.StepResponse
import org.munchies.entity.StepEntity
import org.munchies.mapper.toDomain
import org.munchies.repository.RecipeRepository
import org.munchies.repository.StepRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class RecipeStepService(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository
) {

    // create

    @Transactional
    suspend fun create(userId: String, recipeId: Long, step: StepRequest): StepResponse {
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

    suspend fun readAll(userId: String, recipeId: Long) = stepRepository
        .findAllByUserIdAndRecipeId(userId, recipeId)
        .map { it.toDomain() }
        .toList()

    suspend fun read(userId: String, id: Long, recipeId: Long) = stepRepository
        .findByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
        .toDomain()

    // update

    @Transactional
    suspend fun update(userId: String, id: Long, recipeId: Long, stepUpdate: StepRequest): StepResponse {
        val step = stepRepository
            .findByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
            .apply {
                number = stepUpdate.number
                title = stepUpdate.title
                description = stepUpdate.description
                updatedAt = Date().time
            }

        return stepRepository
            .save(step)
            .toDomain()
    }

    // delete

    @Transactional
    suspend fun delete(userId: String, id: Long, recipeId: Long) {
        stepRepository.apply {
            existsByIdAndUserIdAndRecipeIdOrThrow(id, userId, recipeId)
            deleteByIdAndUserId(id, userId)
        }
    }
}
