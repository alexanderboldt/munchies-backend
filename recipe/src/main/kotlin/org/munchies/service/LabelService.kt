package org.munchies.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.munchies.domain.LabelRequest
import org.munchies.domain.LabelResponse
import org.munchies.repository.LabelRepository
import org.munchies.mapper.toDomain
import org.munchies.entity.LabelEntity
import org.munchies.util.orThrowBadRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class LabelService(private val labelRepository: LabelRepository) {

    // create

    suspend fun create(userId: String, label: LabelRequest): LabelResponse {
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

    suspend fun readAll(userId: String) = labelRepository
        .findAllByUserId(userId, Sort.unsorted())
        .map { it.toDomain() }
        .toList()

    suspend fun read(userId: String, id: Long) = labelRepository
        .findByIdAndUserId(id, userId)
        .orThrowBadRequest()
        .toDomain()

    // update

    @Transactional
    suspend fun update(userId: String, id: Long, labelUpdate: LabelRequest): LabelResponse {
        val label = labelRepository
            .findByIdAndUserId(id, userId)
            .orThrowBadRequest()
            .apply {
                name = labelUpdate.name
                updatedAt = Date().time
            }

        return labelRepository
            .save(label)
            .toDomain()
    }

    // delete

    @Transactional
    suspend fun delete(userId: String, id: Long) {
        labelRepository.apply {
            existsByIdAndUserId(id, userId).orThrowBadRequest()
            deleteByIdAndUserId(id, userId)
        }
    }
}
