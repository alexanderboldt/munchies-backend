package com.alex.munchies.repository.recipe

import com.alex.munchies.exception.BadRequestException
import com.alex.munchies.repository.Repository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<RecipeEntity, Long>, Repository<RecipeEntity> {

    fun findByIdAndUserIdOrThrow(id: Long, userId: String): RecipeEntity {
        return findByIdAndUserId(id, userId) ?: throw BadRequestException()
    }

    fun findAllByUserId(userId: String, sort: Sort): List<RecipeEntity>

    fun findAllByUserId(userId: String, page: Pageable): List<RecipeEntity>
}
