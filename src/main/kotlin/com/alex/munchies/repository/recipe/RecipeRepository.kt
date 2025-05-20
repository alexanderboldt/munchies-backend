package com.alex.munchies.repository.recipe

import com.alex.munchies.repository.Repository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<DbModelRecipe, Long>, Repository<DbModelRecipe> {

    fun findAllByUserId(userId: String, sort: Sort): List<DbModelRecipe>
    fun findAllByUserId(userId: String, page: Pageable): List<DbModelRecipe>
}