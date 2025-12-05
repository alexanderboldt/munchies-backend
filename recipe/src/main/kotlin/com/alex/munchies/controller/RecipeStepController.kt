package com.alex.munchies.controller

import com.alex.munchies.Header
import com.alex.munchies.Path
import com.alex.munchies.PathParam
import com.alex.munchies.domain.StepRequest
import com.alex.munchies.service.StepService
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

@Suppress("unused")
@RestController
class RecipeStepController(private val stepService: StepService) {

    // create

    @PostMapping(Path.RECIPES_STEPS, version = "1")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @RequestBody step: StepRequest
    ) = stepService.create(userId, recipeId, step)

    // read

    @GetMapping(Path.RECIPES_STEPS, version = "1")
    fun readAll(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long
    ) = stepService.readAll(userId, recipeId)

    @GetMapping(Path.RECIPES_STEPS_ID, version = "1")
    fun read(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long
    ) = stepService.read(userId, id, recipeId)

    // update

    @PutMapping(Path.RECIPES_STEPS_ID, version = "1")
    fun update(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long,
        @RequestBody step: StepRequest
    ) = stepService.update(userId, id, recipeId, step)

    // delete

    @DeleteMapping(Path.RECIPES_STEPS_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.RECIPE_ID) recipeId: Long,
        @PathVariable(PathParam.STEP_ID) id: Long
    ) = stepService.delete(userId, id, recipeId)
}
