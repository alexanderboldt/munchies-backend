package com.alex.munchies.repository

import com.alex.munchies.entity.LabelEntity
import com.alex.munchies.util.BadRequestException
import org.springframework.data.repository.CrudRepository

interface LabelRepository : CrudRepository<LabelEntity, Long>, Repository<LabelEntity> {

    fun existsByIdAndUserIdOrThrow(id: Long, userId: String) {
        if (!existsByIdAndUserId(id, userId)) throw BadRequestException()
    }

    fun findByIdAndUserIdOrThrow(id: Long, userId: String): LabelEntity {
        return findByIdAndUserId(id, userId) ?: throw BadRequestException()
    }
}
