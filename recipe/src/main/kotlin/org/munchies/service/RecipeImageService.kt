package org.munchies.service

import kotlinx.coroutines.flow.Flow
import org.munchies.S3Bucket
import org.munchies.client.FileClient
import org.munchies.domain.RecipeResponse
import org.munchies.mapper.toDomain
import org.munchies.repository.RecipeRepository
import org.munchies.util.orThrowBadRequest
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeImageService(
    private val fileClient: FileClient,
    private val recipeRepository: RecipeRepository
) {

    @Transactional
    suspend fun uploadImage(userId: String, id: Long, image: FilePart): RecipeResponse {
        // check if the recipe exists
        val recipe = recipeRepository
            .findByIdAndUserId(id, userId)
            .orThrowBadRequest()

        // 1. if there is already an image saved, throw BadRequestException
        (recipe.filename == null).orThrowBadRequest()

        // 2. upload the image and get the filename
        val response = fileClient.upload(S3Bucket.RECIPE, image)

        // 3. update the recipe with the filename and return it
        return recipeRepository
            .save(recipe.copy(filename = response.filename))
            .toDomain()
    }

    suspend fun downloadImage(userId: String, id: Long): Pair<Flow<DataBuffer>,String> {
        // check if the recipe and the image are existing
        val filename = recipeRepository
            .findByIdAndUserId(id, userId)
            ?.filename
            .orThrowBadRequest()

        // download the file and return it with the filename as a pair
        return fileClient.download(S3Bucket.RECIPE, filename) to filename
    }

    @Transactional
    suspend fun deleteImage(userId: String, id: Long) {
        // check if the recipe and the image are existing
        val recipe = recipeRepository
            .findByIdAndUserId(id, userId)
            .orThrowBadRequest()

        val filename = recipe.filename.orThrowBadRequest()

        // delete the file
        fileClient.delete(S3Bucket.RECIPE, filename)

        // update the entity
        recipeRepository.save(recipe.copy(filename = null))
    }
}
