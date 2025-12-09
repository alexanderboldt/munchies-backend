package org.munchies.controller

import org.munchies.Header
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.domain.RecipeRequest
import org.munchies.service.RecipeService
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class RecipeController(private val recipeService: RecipeService) {

    // create

    @PostMapping(Path.RECIPES, version = "1")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader(Header.USER_ID) userId: String,
        @RequestBody recipe: RecipeRequest
    ) = recipeService.create(userId, recipe)

    // read

    @GetMapping(Path.RECIPES, version = "1")
    fun readAll(
        @RequestHeader(Header.USER_ID) userId: String,
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ) = recipeService.readAll(userId, sort, pageNumber, pageSize)

    @GetMapping(Path.RECIPES_ID, version = "1")
    fun read(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) id: Long
    ) = recipeService.read(userId, id)

    // update

    @PutMapping(Path.RECIPES_ID, version = "1")
    fun update(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) id: Long,
        @RequestBody recipeNew: RecipeRequest
    ) = recipeService.update(userId, id, recipeNew)

    // delete

    @DeleteMapping(Path.RECIPES, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAll() = recipeService.deleteAll()

    @DeleteMapping(Path.RECIPES_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) id: Long
    ) = recipeService.delete(userId, id)
}
