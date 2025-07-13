package com.alex.munchies.service

import com.alex.munchies.domain.Label
import com.alex.munchies.exception.LabelNotFoundException
import com.alex.munchies.repository.label.LabelRepository
import com.alex.munchies.repository.label.plus
import com.alex.munchies.repository.label.toDomain
import com.alex.munchies.repository.label.toEntity
import org.springframework.stereotype.Service

@Service
class LabelService(
    private val userService: UserService,
    private val labelRepository: LabelRepository
) {

    fun create(label: Label): Label {
        return labelRepository.save(label.toEntity(userService.userId)).toDomain()
    }

    fun readAll(): List<Label> {
        return labelRepository.findAllByUserId(userService.userId).map { it.toDomain() }
    }

    fun read(id: Long): Label {
        val label = labelRepository.findByIdAndUserId(id, userService.userId) ?: throw LabelNotFoundException()
        return label.toDomain()
    }

    fun update(id: Long, labelNew: Label): Label {
        val labelExisting = labelRepository.findByIdAndUserId(id, userService.userId) ?: throw LabelNotFoundException()
        return labelRepository.save(labelNew + labelExisting).toDomain()
    }

    fun delete(id: Long) {
        labelRepository.apply {
            if (!existsByIdAndUserId(id, userService.userId)) throw LabelNotFoundException()
            deleteById(id)
        }
    }
}
