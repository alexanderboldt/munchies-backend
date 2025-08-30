package com.alex.munchies.mapper

import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.recipe.RecipeEntity

// from entity to domain

fun RecipeEntity.toDomain() = Recipe(id, userId, labelId, title, description, duration, filename, createdAt, updatedAt)
