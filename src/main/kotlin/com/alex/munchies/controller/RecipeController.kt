package com.alex.munchies.controller

import com.alex.munchies.exception.RecipeNotFoundException
import com.alex.munchies.repository.TheMealDbClient
import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelMeal
import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.recipe.RecipeRepository
import com.alex.munchies.repository.mapping.mergeDbModel
import com.alex.munchies.repository.mapping.newDbModel
import com.alex.munchies.repository.mapping.toApiModel
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/recipes")
class RecipeController(
    private val userService: UserService,
    private val recipeRepository: RecipeRepository,
    private val theMealDbClient: TheMealDbClient
) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody recipe: ApiModelRecipe): ApiModelRecipe {
        return recipeRepository.save(recipe.newDbModel(userService.userId)).toApiModel()
    }

    @PostMapping("/themealdb")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromTheMealDb(@RequestBody meal: ApiModelMeal): ApiModelRecipe {
        val recipe = theMealDbClient
            .getMeal(meal.idMeal)
            .meals
            .first()
            .newDbModel(userService.userId)

        return recipeRepository.save(recipe).toApiModel()
    }

    // read

    @GetMapping
    fun readAll(
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ): List<ApiModelRecipe> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userService.userId, PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userService.userId, sort)
        }.map { it.toApiModel() }
    }

    @GetMapping("{id}")
    fun read(@PathVariable("id") id: Long): ApiModelRecipe {
        val recipe = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()
        return recipe.toApiModel()
    }

    // update

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody recipeNew: ApiModelRecipe): ApiModelRecipe {
        val recipeExisting = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()
        return recipeRepository.save(recipeNew.mergeDbModel(recipeExisting)).toApiModel()
    }

    // delete

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long) {
        recipeRepository.apply {
            if (!existsByIdAndUserId(id, userService.userId)) throw RecipeNotFoundException()
            deleteById(id)
        }
    }
}