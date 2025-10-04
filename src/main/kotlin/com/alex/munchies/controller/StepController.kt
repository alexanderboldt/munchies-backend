package com.alex.munchies.controller

import com.alex.munchies.domain.Step
import com.alex.munchies.service.StepService
import com.alex.munchies.util.Path
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
    fun create(@PathVariable("recipe_id") recipeId: Long, @RequestBody step: Step) = stepService.create(recipeId, step)
}
