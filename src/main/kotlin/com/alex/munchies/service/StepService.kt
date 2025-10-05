package com.alex.munchies.service

import com.alex.munchies.domain.Step
import com.alex.munchies.entity.StepEntity
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.StepRepository
import org.springframework.stereotype.Service
import java.util.Date

@Service
class StepService(
    private val userService: UserService,
    private val stepRepository: StepRepository
) {

    // create

    fun create(recipeId: Long, step: Step): Step {
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

        return stepRepository
            .save(entity)
            .toDomain()
    }

    // read

    fun readAll(recipeId: Long): List<Step> {
        return stepRepository
            .findAllByUserIdAndRecipeId(userService.userId, recipeId)
            .map { it.toDomain() }
    }

    fun read(id: Long, recipeId: Long): Step {
        return stepRepository
            .findByIdAndUserIdAndRecipeIdOrThrow(id, userService.userId, recipeId)
            .toDomain()
    }

    // delete

    fun delete(id: Long, recipeId: Long) {
        stepRepository.apply {
            existsByIdAndUserIdAndRecipeIdOrThrow(id, userService.userId, recipeId)
            deleteByIdAndUserId(id, userService.userId)
        }
    }
}
