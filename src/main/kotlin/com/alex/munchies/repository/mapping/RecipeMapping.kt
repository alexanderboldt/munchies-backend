package com.alex.munchies.repository.mapping

import com.alex.munchies.repository.api.ApiModelMealGet
import com.alex.munchies.repository.api.ApiModelRecipeGet
import com.alex.munchies.repository.api.ApiModelRecipePost
import com.alex.munchies.repository.database.DbModelRecipe
import java.util.Date

// from api to database

fun ApiModelMealGet.toDbModel(userId: String) = DbModelRecipe(0, userId, strMeal, strCategory, 0, Date().time, Date().time)

fun ApiModelRecipePost.toDbModel(userId: String) = DbModelRecipe(0, userId, title, description, duration, Date().time, Date().time)

fun ApiModelRecipePost.toDbModel(dbModelExisting: DbModelRecipe) = DbModelRecipe(dbModelExisting.id, dbModelExisting.userId, title, description, duration, dbModelExisting.createdAt, Date().time)

// from database to api

fun Iterable<DbModelRecipe>.toApiModelGet() = map { it.toApiModelGet() }

fun DbModelRecipe.toApiModelGet() = ApiModelRecipeGet(id, userId, title, description, duration, createdAt, updatedAt)