package org.munchies.controller

import org.munchies.Header
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.domain.StepRequest
import org.munchies.service.RecipeStepService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class RecipeStepController(private val recipeStepService: RecipeStepService) {

    // create

    @PostMapping(Path.RECIPES_STEPS, version = "1")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @RequestBody step: StepRequest
    ) = recipeStepService.create(userId, recipeId, step)

    // read

    @GetMapping(Path.RECIPES_STEPS, version = "1")
    fun readAll(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long
    ) = recipeStepService.readAll(userId, recipeId)

    @GetMapping(Path.RECIPES_STEPS_ID, version = "1")
    fun read(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long
    ) = recipeStepService.read(userId, id, recipeId)

    // update

    @PutMapping(Path.RECIPES_STEPS_ID, version = "1")
    fun update(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long,
        @RequestBody step: StepRequest
    ) = recipeStepService.update(userId, id, recipeId, step)

    // delete

    @DeleteMapping(Path.RECIPES_STEPS_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long
    ) = recipeStepService.delete(userId, id, recipeId)
}
