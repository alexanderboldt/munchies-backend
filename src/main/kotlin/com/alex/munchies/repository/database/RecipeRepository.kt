package com.alex.munchies.repository.database

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

interface RecipeRepository : CrudRepository<DbModelRecipe, Long> {

    fun findAllByUserId(userId: String, sort: Sort): List<DbModelRecipe>
    fun findAllByUserId(userId: String, page: Pageable): List<DbModelRecipe>

    fun findByIdAndUserId(id: Long, userId: String): DbModelRecipe?
}

class RecipeNotFoundException : ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found with the given id!")

fun RecipeRepository.findByIdAndUserIdOrThrowException(id: Long, userId: String): DbModelRecipe {
    return findByIdAndUserId(id, userId) ?: throw RecipeNotFoundException()
}