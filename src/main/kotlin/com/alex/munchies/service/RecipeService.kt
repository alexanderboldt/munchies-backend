package com.alex.munchies.service

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.exception.LabelNotFoundException
import com.alex.munchies.exception.RecipeNotFoundException
import com.alex.munchies.repository.recipe.RecipeRepository
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.label.LabelRepository
import com.alex.munchies.repository.recipe.RecipeEntity
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
            .findByIdAndUserId(id, userService.userId)
            ?.toDomain()
            ?: throw RecipeNotFoundException()
    }

    // update

    @Transactional
    fun update(id: Long, recipeUpdate: Recipe): Recipe {
        // check if the label-id exists
        recipeUpdate.labelId?.let {
            if (!labelRepository.existsByIdAndUserId(it, userService.userId)) throw LabelNotFoundException()
        }

        return recipeRepository
            .findByIdAndUserId(id, userService.userId)
            ?.apply {
                labelId = recipeUpdate.labelId
                title = recipeUpdate.title
                description = recipeUpdate.description
                duration = recipeUpdate.duration
                updatedAt = Date().time
            }?.toDomain()
            ?: throw RecipeNotFoundException()
    }

    // delete

    fun deleteAll() {
        recipeRepository.deleteAll()
    }

    fun delete(id: Long) {
        val recipeExisting = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()

        // delete an existing image from the storage and the recipe
        recipeExisting.filename?.let { s3Service.deleteFile(S3Bucket.RECIPE, it) }
        recipeRepository.deleteById(id)
    }
}
