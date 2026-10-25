package org.munchies.repository

import org.munchies.repository.entity.RecipeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RecipeRepository : CoroutineCrudRepository<RecipeEntity, Long>, BaseRepository<RecipeEntity>
