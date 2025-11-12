package com.alex.munchies.repository

import com.alex.munchies.entity.RecipeEntity
import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<RecipeEntity, Long>, BaseRepository<RecipeEntity>
