package com.alex.munchies.controller

import com.alex.munchies.repository.TheMealDbClient
import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelMeal
import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.RecipeRepository
import com.alex.munchies.repository.database.findByIdAndUserIdOrThrowException
import com.alex.munchies.repository.mapping.mergeDbModel
import com.alex.munchies.repository.mapping.newDbModel
import com.alex.munchies.repository.mapping.toApiModel
import com.alex.munchies.repository.mapping.toApiModelGet
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
    fun postRecipe(@RequestBody recipe: ApiModelRecipe): ApiModelRecipe {
        return recipeRepository.save(recipe.newDbModel(userService.getUserId())).toApiModel()
    }

    @PostMapping("/themealdb")
    @ResponseStatus(HttpStatus.CREATED)
    fun postRecipeFromTheMealDb(@RequestBody meal: ApiModelMeal): ApiModelRecipe {
        val recipe = theMealDbClient
            .getMeal(meal.idMeal)
            .meals
            .first()
            .newDbModel(userService.getUserId())

        return recipeRepository.save(recipe).toApiModel()
    }

    // read

    @GetMapping
    fun getAllRecipes(
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ): List<ApiModelRecipe> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userService.getUserId(), PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userService.getUserId(), sort)
        }.toList().toApiModelGet()
    }

    @GetMapping("{id}")
    fun getRecipe(@PathVariable("id") id: Long): ApiModelRecipe {
        return recipeRepository.findByIdAndUserIdOrThrowException(id, userService.getUserId()).toApiModel()
    }

    // update

    @PutMapping("{id}")
    fun updateRecipe(@PathVariable("id") id: Long, @RequestBody recipe: ApiModelRecipe): ApiModelRecipe {
        val recipeUpdated = recipeRepository
            .findByIdAndUserIdOrThrowException(id, userService.getUserId())
            .let { recipe.mergeDbModel(it) }

        return recipeRepository.save(recipeUpdated).toApiModel()
    }

    // delete

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecipe(@PathVariable("id") id: Long) {
        recipeRepository.apply {
            findByIdAndUserIdOrThrowException(id, userService.getUserId())
            deleteById(id)
        }
    }
}