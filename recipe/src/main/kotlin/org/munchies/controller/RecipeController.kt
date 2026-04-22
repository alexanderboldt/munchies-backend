package org.munchies.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
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

@RestController
class RecipeController(private val recipeService: RecipeService) {

    // create

    @PostMapping(Path.RECIPES, version = "1")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Valid @RequestBody recipe: RecipeRequest
    ) = recipeService.create(userId, recipe)

    // read

    @GetMapping(Path.RECIPES, version = "1")
    suspend fun readAll(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ) = recipeService.readAll(userId, sort, pageNumber, pageSize)

    @GetMapping(Path.RECIPES_ID, version = "1")
    suspend fun read(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long
    ) = recipeService.read(userId, id)

    // update

    @PutMapping(Path.RECIPES_ID, version = "1")
    suspend fun update(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long,
        @Valid @RequestBody recipeNew: RecipeRequest
    ) = recipeService.update(userId, id, recipeNew)

    // delete

    @DeleteMapping(Path.RECIPES, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteAll() = recipeService.deleteAll()

    @DeleteMapping(Path.RECIPES_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long
    ) = recipeService.delete(userId, id)
}
