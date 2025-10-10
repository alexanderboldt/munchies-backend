package com.alex.munchies.mapper

import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.entity.RecipeEntity

// from entity to domain

fun RecipeEntity.toDomain() = RecipeResponse(id, userId, labelId, title, description, duration, filename, createdAt, updatedAt)
