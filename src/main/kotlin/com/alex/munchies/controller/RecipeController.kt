package com.alex.munchies.controller

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.service.RecipeService
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@RestController
@RequestMapping("api/v1/recipes")
class RecipeController(private val recipeService: RecipeService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody recipe: Recipe) = recipeService.create(recipe)

    @PostMapping("/themealdb")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromTheMealDb(@RequestBody meal: Meal) = recipeService.createFromTheMealDb(meal)

    // read

    @GetMapping
    fun readAll(
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ) = recipeService.readAll(sort, pageNumber, pageSize)

    @GetMapping("{id}")
    fun read(@PathVariable("id") id: Long) = recipeService.read(id)

    // update

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody recipeNew: Recipe) = recipeService.update(id, recipeNew)

    // delete

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long) = recipeService.delete(id)
}