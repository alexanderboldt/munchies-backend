package org.munchies.mapper

import org.munchies.domain.LabelResponse
import org.munchies.entity.LabelEntity

// from entity to domain

fun LabelEntity.toDomain() = LabelResponse(id, userId, name, createdAt, updatedAt)
