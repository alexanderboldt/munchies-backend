package com.alex.munchies.mapper

import com.alex.munchies.domain.Label
import com.alex.munchies.entity.LabelEntity

// from entity to domain

fun LabelEntity.toDomain() = Label(id, userId, name, createdAt, updatedAt)
