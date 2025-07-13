package com.alex.munchies.service

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.exception.RecipeNotFoundException
import com.alex.munchies.repository.recipe.RecipeRepository
import com.alex.munchies.repository.recipe.plus
import com.alex.munchies.repository.recipe.toDomain
import com.alex.munchies.repository.recipe.toEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val userService: UserService,
    private val recipeRepository: RecipeRepository,
    private val theMealDbClient: TheMealDbClient
) {

    fun create(recipe: Recipe): Recipe {
        return recipeRepository.save(recipe.toEntity(userService.userId)).toDomain()
    }

    fun createFromTheMealDb(meal: Meal): Recipe {
        val recipe = theMealDbClient
            .getMeal(meal.idMeal)
            .meals
            .first()
            .toEntity(userService.userId)

        return recipeRepository.save(recipe).toDomain()
    }

    fun readAll(sort: Sort = Sort.unsorted(), pageNumber: Int = -1, pageSize: Int = -1): List<Recipe> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userService.userId, PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userService.userId, sort)
        }.map { it.toDomain() }
    }

    fun read(id: Long): Recipe {
        val recipe = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()
        return recipe.toDomain()
    }

    fun update(id: Long, recipeNew: Recipe): Recipe {
        val recipeExisting = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()
        return recipeRepository.save(recipeNew + recipeExisting).toDomain()
    }

    fun delete(id: Long) {
        recipeRepository.apply {
            if (!existsByIdAndUserId(id, userService.userId)) throw RecipeNotFoundException()
            deleteById(id)
        }
    }
}
