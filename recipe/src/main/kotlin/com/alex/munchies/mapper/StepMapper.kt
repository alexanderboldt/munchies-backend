package com.alex.munchies.mapper

import com.alex.munchies.domain.StepResponse
import com.alex.munchies.entity.StepEntity

// from entity to domain

fun StepEntity.toDomain() = StepResponse(id, userId, recipeId, number, title, description, createdAt, updatedAt)
