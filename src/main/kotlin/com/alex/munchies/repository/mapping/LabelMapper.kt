package com.alex.munchies.repository.mapping

import com.alex.munchies.repository.api.ApiModelLabel
import com.alex.munchies.repository.database.label.DbModelLabel
import java.util.Date

// from api to database

fun ApiModelLabel.newDbModel(userId: String) = DbModelLabel(0, userId, name, Date().time, Date().time)

operator fun ApiModelLabel.plus(existing: DbModelLabel) = DbModelLabel(existing.id, existing.userId, name,existing.createdAt, Date().time)

// from database to api

fun DbModelLabel.toApiModel() = ApiModelLabel(id, userId, name, createdAt, updatedAt)