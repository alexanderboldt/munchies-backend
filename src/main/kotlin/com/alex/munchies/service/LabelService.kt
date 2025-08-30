package com.alex.munchies.service

import com.alex.munchies.domain.Label
import com.alex.munchies.exception.LabelNotFoundException
import com.alex.munchies.repository.label.LabelRepository
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.label.LabelEntity
import jakarta.transaction.Transactional
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
            .findAllByUserId(userService.userId)
            .map { it.toDomain() }
    }

    fun read(id: Long): Label {
        return labelRepository
            .findByIdAndUserId(id, userService.userId)
            ?.toDomain()
            ?: throw LabelNotFoundException()
    }

    // update

    @Transactional
    fun update(id: Long, labelUpdate: Label): Label {
        return labelRepository
            .findByIdAndUserId(id, userService.userId)
            ?.apply {
                name = labelUpdate.name
                updatedAt = Date().time
            }?.toDomain()
            ?: throw LabelNotFoundException()
    }

    // delete

    fun delete(id: Long) {
        labelRepository.apply {
            if (!existsByIdAndUserId(id, userService.userId)) throw LabelNotFoundException()
            deleteById(id)
        }
    }
}
