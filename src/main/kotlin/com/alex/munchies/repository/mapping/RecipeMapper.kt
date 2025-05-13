package com.alex.munchies.repository.mapping

import com.alex.munchies.repository.api.ApiModelMeal
import com.alex.munchies.repository.api.ApiModelRecipe
import com.alex.munchies.repository.database.recipe.DbModelRecipe
import java.util.Date

// from api to database

fun ApiModelMeal.newDbModel(userId: String) = DbModelRecipe(0, userId, null, strMeal, strCategory, 0, Date().time, Date().time)

fun ApiModelRecipe.newDbModel(userId: String) = DbModelRecipe(0, userId, labelId, title, description, duration, Date().time, Date().time)

fun ApiModelRecipe.mergeDbModel(existing: DbModelRecipe) = DbModelRecipe(existing.id, existing.userId, labelId, title, description, duration, existing.createdAt, Date().time)

// from database to api

fun DbModelRecipe.toApiModel() = ApiModelRecipe(id, userId, labelId, title, description, duration, createdAt, updatedAt)