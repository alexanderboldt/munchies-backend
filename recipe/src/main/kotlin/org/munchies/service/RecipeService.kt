package org.munchies.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.munchies.domain.RecipeRequest
import org.munchies.domain.RecipeResponse
import org.munchies.repository.RecipeRepository
import org.munchies.mapper.toDomain
import org.munchies.repository.LabelRepository
import org.munchies.entity.RecipeEntity
import org.munchies.util.orThrowBadRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class RecipeService(
    private val s3Service: S3Service,
    private val labelRepository: LabelRepository,
    private val recipeRepository: RecipeRepository
) {
    // create

    suspend fun create(userId: String, recipe: RecipeRequest): RecipeResponse {
        val entity = RecipeEntity(
            0,
            userId,
            recipe.labelId,
            recipe.title,
            recipe.description,
            recipe.duration,
            null,
            Date().time,
            Date().time
        )

        return recipeRepository
            .save(entity)
            .toDomain()
    }

    // read

    suspend fun readAll(userId: String, sort: Sort = Sort.unsorted(), pageNumber: Int = -1, pageSize: Int = -1): List<RecipeResponse> {
        return when (pageNumber >= 0 && pageSize >= 1) {
            true -> recipeRepository.findAllByUserId(userId, PageRequest.of(pageNumber, pageSize, sort))
            false -> recipeRepository.findAllByUserId(userId, sort)
        }.map { it.toDomain() }.toList()
    }

    suspend fun read(userId: String, id: Long) = recipeRepository
        .findByIdAndUserId(id, userId)
        .orThrowBadRequest()
        .toDomain()

    // update

    @Transactional
    suspend fun update(userId: String, id: Long, recipeUpdate: RecipeRequest): RecipeResponse {
        // check if the label-id exists
        recipeUpdate.labelId?.let {
            labelRepository
                .existsByIdAndUserId(it, userId)
                .orThrowBadRequest()
        }

        val recipe = recipeRepository
            .findByIdAndUserId(id, userId)
            .orThrowBadRequest()
            .apply {
                labelId = recipeUpdate.labelId
                title = recipeUpdate.title
                description = recipeUpdate.description
                duration = recipeUpdate.duration
                updatedAt = Date().time
            }

        return recipeRepository
            .save(recipe)
            .toDomain()
    }

    // delete

    suspend fun deleteAll() {
        recipeRepository.deleteAll()
    }

    @Transactional
    suspend fun delete(userId: String, id: Long) {
        // try to delete the file from the storage
        recipeRepository
            .findByIdAndUserId(id, userId)
            .orThrowBadRequest()
            .filename
            ?.let { s3Service.deleteFile(S3Bucket.RECIPE, it) }

        // delete the recipe
        recipeRepository.deleteByIdAndUserId(id, userId)
    }
}
