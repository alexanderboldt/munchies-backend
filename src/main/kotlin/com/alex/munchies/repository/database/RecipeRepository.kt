package com.alex.munchies.repository.database

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<DbModelRecipe, Long> {

    fun existsByIdAndUserId(id: Long, userId: String): Boolean

    fun findAllByUserId(userId: String, sort: Sort): List<DbModelRecipe>
    fun findAllByUserId(userId: String, page: Pageable): List<DbModelRecipe>

    fun findByIdAndUserId(id: Long, userId: String): DbModelRecipe?
}