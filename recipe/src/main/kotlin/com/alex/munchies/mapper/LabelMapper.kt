package com.alex.munchies.mapper

import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.entity.LabelEntity

// from entity to domain

fun LabelEntity.toDomain() = LabelResponse(id, userId, name, createdAt, updatedAt)
