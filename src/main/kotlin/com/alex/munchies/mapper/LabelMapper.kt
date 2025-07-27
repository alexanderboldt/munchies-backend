package com.alex.munchies.mapper

import com.alex.munchies.domain.Label
import com.alex.munchies.repository.label.LabelEntity
import java.util.Date

// from domain to entity

fun Label.toEntity(userId: String) = LabelEntity(
    0,
    userId,
    name,
    Date().time,
    Date().time
)

operator fun Label.plus(existing: LabelEntity) = LabelEntity(
    existing.id,
    existing.userId,
    name,
    existing.createdAt,
    Date().time
)

// from entity to domain

fun LabelEntity.toDomain() = Label(id, userId, name, createdAt, updatedAt)
