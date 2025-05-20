package com.alex.munchies.repository.recipe

import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import java.util.Date

// from domain to entity

fun Meal.toEntity(userId: String) = RecipeEntity(0, userId, null, strMeal, strCategory, 0, Date().time, Date().time)

fun Recipe.toEntity(userId: String) = RecipeEntity(0, userId, labelId, title, description, duration, Date().time, Date().time)

operator fun Recipe.plus(existing: RecipeEntity) = RecipeEntity(existing.id, existing.userId, labelId, title, description, duration, existing.createdAt, Date().time)

// from entity to domain

fun RecipeEntity.toDomain() = Recipe(id, userId, labelId, title, description, duration, createdAt, updatedAt)