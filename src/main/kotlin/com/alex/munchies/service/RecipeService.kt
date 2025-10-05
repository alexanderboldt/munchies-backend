package com.alex.munchies.service

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.RecipeRepository
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.LabelRepository
import com.alex.munchies.entity.RecipeEntity
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.Date

@Service
class RecipeService(
    private val userService: UserService,
    private val s3Service: S3Service,
    private val labelRepository: LabelRepository,
    private val recipeRepository: RecipeRepository,
    private val theMealDbClient: TheMealDbClient
) {
    // create

    fun create(recipe: Recipe): Recipe {
        val entity = RecipeEntity(
            0,
            userService.userId,
            recipe.labelId,
            recipe.title,
            recipe.description,
            recipe.duration,
            null,
            Date().time,
            Date().time
        )

        return recipeRepository
            .save(entity)
            .toDomain()
    }

    fun createFromTheMealDb(meal: Meal): Recipe {
        val recipe = theMealDbClient
            .getMeal(meal.idMeal)
            .meals
            .first()
            .run {
                RecipeEntity(
                    0,
                    userService.userId,
                    null,
                    strMeal,
                    strCategory,
                    0,
                    null,
                    Date().time,
                    Date().time
                )
            }

        return recipeRepository
            .save(recipe)
            .toDomain()
    }

    // read

    fun readAll(sort: Sort = Sort.unsorted(), pageNumber: Int = -1, pageSize: Int = -1): List<Recipe> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userService.userId, PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userService.userId, sort)
        }.map { it.toDomain() }
    }

    fun read(id: Long): Recipe {
        return recipeRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .toDomain()
    }

    // update

    @Transactional
    fun update(id: Long, recipeUpdate: Recipe): Recipe {
        // check if the label-id exists
        recipeUpdate.labelId?.let {
            labelRepository.existsByIdAndUserIdOrThrow(it, userService.userId)
        }

        return recipeRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .apply {
                labelId = recipeUpdate.labelId
                title = recipeUpdate.title
                description = recipeUpdate.description
                duration = recipeUpdate.duration
                updatedAt = Date().time
            }.toDomain()
    }

    // delete

    fun deleteAll() {
        recipeRepository.deleteAll()
    }

    fun delete(id: Long) {
        // try to delete the file from the storage
        recipeRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .filename
            ?.let { s3Service.deleteFile(S3Bucket.RECIPE, it) }

        // delete the recipe
        recipeRepository.deleteByIdAndUserId(id, userService.userId)
    }
}
