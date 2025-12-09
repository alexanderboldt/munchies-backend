package org.munchies.mapper

import org.munchies.domain.StepResponse
import org.munchies.entity.StepEntity

// from entity to domain

fun StepEntity.toDomain() = StepResponse(id, userId, recipeId, number, title, description, createdAt, updatedAt)
