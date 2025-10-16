package com.alex.munchies.controller

import com.alex.munchies.domain.StepRequest
import com.alex.munchies.service.StepService
import com.alex.munchies.util.Path
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
@RequestMapping(Path.STEP)
class StepController(private val stepService: StepService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable recipeId: Long, @RequestBody step: StepRequest) = stepService.create(recipeId, step)

    // read

    @GetMapping
    fun readAll(@PathVariable recipeId: Long) = stepService.readAll(recipeId)

    @GetMapping(Path.ID)
    fun read(@PathVariable recipeId: Long, @PathVariable id: Long) = stepService.read(id, recipeId)

    // update

    @PutMapping(Path.ID)
    fun update(
        @PathVariable recipeId: Long,
        @PathVariable id: Long,
        @RequestBody step: StepRequest
    ) = stepService.update(id, recipeId, step)

    // delete

    @DeleteMapping(Path.ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable recipeId: Long, @PathVariable id: Long) = stepService.delete(id, recipeId)
}
