package com.alex.munchies.mapper

import com.alex.munchies.domain.Step
import com.alex.munchies.entity.StepEntity

// from entity to domain

fun StepEntity.toDomain() = Step(id, userId, recipeId, number, title, description, createdAt, updatedAt)
