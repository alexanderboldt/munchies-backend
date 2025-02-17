package com.alex.munchies.controller

import com.alex.munchies.repository.TheMealDbClient
import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelRecipeGet
import com.alex.munchies.repository.api.ApiModelRecipePost
import com.alex.munchies.repository.api.ApiModelTheMealDbPost
import com.alex.munchies.repository.database.RecipeRepository
import com.alex.munchies.repository.database.findByIdAndUserIdOrThrowException
import com.alex.munchies.repository.mapping.toApiModelGet
import com.alex.munchies.repository.mapping.toDbModel
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
    fun postRecipe(@RequestBody recipe: ApiModelRecipePost): ApiModelRecipeGet {
        return recipeRepository.save(recipe.toDbModel(userService.getUserId())).toApiModelGet()
    }

    @PostMapping("/themealdb")
    @ResponseStatus(HttpStatus.CREATED)
    fun postRecipeFromTheMealDb(@RequestBody recipe: ApiModelTheMealDbPost): ApiModelRecipeGet {
        val meal = theMealDbClient.getMeal(recipe.id).meals.first()
        return recipeRepository.save(meal.toDbModel(userService.getUserId())).toApiModelGet()
    }

    // read

    @GetMapping
    fun getAllRecipes(
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ): List<ApiModelRecipeGet> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userService.getUserId(), PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userService.getUserId(), sort)
        }.toList().toApiModelGet()
    }

    @GetMapping("{id}")
    fun getRecipe(@PathVariable("id") id: Long): ApiModelRecipeGet {
        return recipeRepository.findByIdAndUserIdOrThrowException(id, userService.getUserId()).toApiModelGet()
    }

    // update

    @PutMapping("{id}")
    fun updateRecipe(@PathVariable("id") id: Long, @RequestBody recipe: ApiModelRecipePost): ApiModelRecipeGet {
        val recipeUpdated = recipeRepository
            .findByIdAndUserIdOrThrowException(id, userService.getUserId())
            .let { recipe.toDbModel(it) }

        return recipeRepository.save(recipeUpdated).toApiModelGet()
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