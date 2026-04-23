package org.munchies.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
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
    suspend fun create(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @Valid @RequestBody step: StepRequest
    ) = recipeStepService.create(userId, recipeId, step)

    // read

    @GetMapping(Path.RECIPES_STEPS, version = "1")
    suspend fun readAll(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) recipeId: Long
    ) = recipeStepService.readAll(userId, recipeId)

    @GetMapping(Path.RECIPES_STEPS_ID, version = "1")
    suspend fun read(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @Positive @PathVariable(PathParam.STEP_ID) id: Long
    ) = recipeStepService.read(userId, id, recipeId)

    // update

    @PutMapping(Path.RECIPES_STEPS_ID, version = "1")
    suspend fun update(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @Positive @PathVariable(PathParam.STEP_ID) id: Long,
        @Valid @RequestBody step: StepRequest
    ) = recipeStepService.update(userId, id, recipeId, step)

    // delete

    @DeleteMapping(Path.RECIPES_STEPS_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @Positive @PathVariable(PathParam.STEP_ID) id: Long
    ) = recipeStepService.delete(userId, id, recipeId)
}
