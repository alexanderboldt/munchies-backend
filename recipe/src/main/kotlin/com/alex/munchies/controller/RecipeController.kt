package com.alex.munchies.controller

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.RecipeRequest
import com.alex.munchies.service.RecipeService
import com.alex.munchies.util.Path
import com.alex.munchies.util.RateLimit
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RateLimiter(name = RateLimit.BASIC)
@RestController
@RequestMapping(Path.RECIPE)
class RecipeController(private val recipeService: RecipeService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody recipe: RecipeRequest) = recipeService.create(recipe)

    @PostMapping(Path.THE_MEAL_DB)
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromTheMealDb(@RequestBody meal: Meal) = recipeService.createFromTheMealDb(meal)

    // read

    @GetMapping
    fun readAll(
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ) = recipeService.readAll(sort, pageNumber, pageSize)

    @GetMapping(Path.ID)
    fun read(@PathVariable id: Long) = recipeService.read(id)

    // update

    @PutMapping(Path.ID)
    fun update(@PathVariable id: Long, @RequestBody recipeNew: RecipeRequest) = recipeService.update(id, recipeNew)

    // delete

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAll() = recipeService.deleteAll()

    @DeleteMapping(Path.ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = recipeService.delete(id)
}
