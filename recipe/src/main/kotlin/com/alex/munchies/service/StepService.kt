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
    private val userService: UserService,
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository
) {

    // create

    fun create(recipeId: Long, step: StepRequest): StepResponse {
        // check if the recipe exists
        recipeRepository.existsByIdAndUserIdOrThrow(recipeId, userService.userId)

        // arrange the information
        val entity = StepEntity(
            0,
            userService.userId,
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

    fun readAll(recipeId: Long) = stepRepository
        .findAllByUserIdAndRecipeId(userService.userId, recipeId)
        .map { it.toDomain() }

    fun read(id: Long, recipeId: Long) = stepRepository
        .findByIdAndUserIdAndRecipeIdOrThrow(id, userService.userId, recipeId)
        .toDomain()

    // update

    @Transactional
    fun update(id: Long, recipeId: Long, stepUpdate: StepRequest) = stepRepository
        .findByIdAndUserIdAndRecipeIdOrThrow(id, userService.userId, recipeId)
        .apply {
            number = stepUpdate.number
            title = stepUpdate.title
            description = stepUpdate.description
            updatedAt = Date().time
        }.toDomain()

    // delete

    fun delete(id: Long, recipeId: Long) {
        stepRepository.apply {
            existsByIdAndUserIdAndRecipeIdOrThrow(id, userService.userId, recipeId)
            deleteByIdAndUserId(id, userService.userId)
        }
    }
}
