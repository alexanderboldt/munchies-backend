package org.munchies.repository

import org.munchies.entity.RecipeEntity
import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<RecipeEntity, Long>, BaseRepository<RecipeEntity>
