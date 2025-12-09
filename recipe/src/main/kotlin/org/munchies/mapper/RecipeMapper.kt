package org.munchies.mapper

import org.munchies.domain.RecipeResponse
import org.munchies.entity.RecipeEntity

// from entity to domain

fun RecipeEntity.toDomain() = RecipeResponse(id, userId, labelId, title, description, duration, filename, createdAt, updatedAt)
