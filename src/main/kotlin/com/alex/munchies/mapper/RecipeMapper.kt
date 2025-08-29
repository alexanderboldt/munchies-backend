package com.alex.munchies.mapper

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.recipe.RecipeEntity
import java.util.Date

// from domain to entity

fun Meal.toEntity(userId: String) = RecipeEntity(
    0,
    userId,
    null,
    strMeal,
    strCategory,
    0,
    null,
    Date().time,
    Date().time
)

fun Recipe.toEntity(userId: String) = RecipeEntity(
    0,
    userId,
    labelId,
    title,
    description,
    duration,
    null,
    Date().time,
    Date().time
)

operator fun Recipe.plus(existing: RecipeEntity) = RecipeEntity(
    existing.id,
    existing.userId,
    labelId,
    title,
    description,
    duration,
    existing.filename,
    existing.createdAt,
    Date().time
)

// from entity to domain

fun RecipeEntity.toDomain() = Recipe(id, userId, labelId, title, description, duration, filename, createdAt, updatedAt)
