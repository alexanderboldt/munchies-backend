package com.alex.munchies.repository.mapping

import com.alex.munchies.repository.api.ApiModelMeal
import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.recipe.DbModelRecipe
import java.util.Date

// from api to database

fun ApiModelMeal.newDbModel(userId: String) = DbModelRecipe(0, userId, strMeal, strCategory, 0, Date().time, Date().time)

fun ApiModelRecipe.newDbModel(userId: String) = DbModelRecipe(0, userId, title, description, duration, Date().time, Date().time)

fun ApiModelRecipe.mergeDbModel(existing: DbModelRecipe) = DbModelRecipe(existing.id, existing.userId, title, description, duration, existing.createdAt, Date().time)

// from database to api

fun Iterable<DbModelRecipe>.toApiModelGet() = map { it.toApiModel() }

fun DbModelRecipe.toApiModel() = ApiModelRecipe(id, userId, title, description, duration, createdAt, updatedAt)