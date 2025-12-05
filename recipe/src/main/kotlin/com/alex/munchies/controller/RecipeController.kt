package com.alex.munchies.controller

import com.alex.munchies.Header
import com.alex.munchies.Path
import com.alex.munchies.domain.RecipeRequest
import com.alex.munchies.service.RecipeService
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
@RequestMapping(Path.RECIPE, version = "1")
class RecipeController(private val recipeService: RecipeService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestHeader(Header.USER_ID) userId: String, @RequestBody recipe: RecipeRequest) = recipeService.create(userId, recipe)

    // read

    @GetMapping
    fun readAll(
        @RequestHeader(Header.USER_ID) userId: String,
        @RequestParam sort: Sort = Sort.unsorted(),
        @RequestParam pageNumber: Int = -1,
        @RequestParam pageSize: Int = -1
    ) = recipeService.readAll(userId, sort, pageNumber, pageSize)

    @GetMapping(Path.ID)
    fun read(@RequestHeader(Header.USER_ID) userId: String, @PathVariable id: Long) = recipeService.read(userId, id)

    // update

    @PutMapping(Path.ID)
    fun update(@RequestHeader(Header.USER_ID) userId: String, @PathVariable id: Long, @RequestBody recipeNew: RecipeRequest) = recipeService.update(userId, id, recipeNew)

    // delete

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAll() = recipeService.deleteAll()

    @DeleteMapping(Path.ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@RequestHeader(Header.USER_ID) userId: String, @PathVariable id: Long) = recipeService.delete(userId, id)
}
