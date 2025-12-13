package org.munchies.repository

import org.munchies.entity.RecipeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RecipeRepository : CoroutineCrudRepository<RecipeEntity, Long>, BaseRepository<RecipeEntity>
