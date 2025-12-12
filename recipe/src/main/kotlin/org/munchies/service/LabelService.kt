package org.munchies.service

import org.munchies.domain.LabelRequest
import org.munchies.domain.LabelResponse
import org.munchies.repository.LabelRepository
import org.munchies.mapper.toDomain
import org.munchies.entity.LabelEntity
import jakarta.transaction.Transactional
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.Date

@Service
class LabelService(private val labelRepository: LabelRepository) {

    // create

    fun create(userId: String, label: LabelRequest): LabelResponse {
        val entity = LabelEntity(
            0,
            userId,
            label.name,
            Date().time,
            Date().time
        )

        return labelRepository
            .save(entity)
            .toDomain()
    }

    // read

    fun readAll(userId: String) = labelRepository
        .findAllByUserId(userId, Sort.unsorted())
        .map { it.toDomain() }

    fun read(userId: String, id: Long) = labelRepository
        .findByIdAndUserIdOrThrow(id, userId)
        .toDomain()

    // update

    @Transactional
    fun update(userId: String, id: Long, labelUpdate: LabelRequest) = labelRepository
        .findByIdAndUserIdOrThrow(id, userId)
        .apply {
            name = labelUpdate.name
            updatedAt = Date().time
        }.toDomain()

    // delete

    fun delete(userId: String, id: Long) {
        labelRepository.apply {
            existsByIdAndUserIdOrThrow(id, userId)
            deleteByIdAndUserId(id, userId)
        }
    }
}
