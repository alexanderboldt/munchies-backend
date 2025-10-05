package com.alex.munchies.service

import com.alex.munchies.domain.Label
import com.alex.munchies.repository.LabelRepository
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.entity.LabelEntity
import jakarta.transaction.Transactional
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.Date

@Service
class LabelService(
    private val userService: UserService,
    private val labelRepository: LabelRepository
) {
    // create

    fun create(label: Label): Label {
        val entity = LabelEntity(
            0,
            userService.userId,
            label.name,
            Date().time,
            Date().time
        )

        return labelRepository
            .save(entity)
            .toDomain()
    }

    // read

    fun readAll(): List<Label> {
        return labelRepository
            .findAllByUserId(userService.userId, Sort.unsorted())
            .map { it.toDomain() }
    }

    fun read(id: Long): Label {
        return labelRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .toDomain()
    }

    // update

    @Transactional
    fun update(id: Long, labelUpdate: Label): Label {
        return labelRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .apply {
                name = labelUpdate.name
                updatedAt = Date().time
            }.toDomain()
    }

    // delete

    fun delete(id: Long) {
        labelRepository.apply {
            existsByIdAndUserIdOrThrow(id, userService.userId)
            deleteByIdAndUserId(id, userService.userId)
        }
    }
}
