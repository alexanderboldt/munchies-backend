package org.munchies.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.munchies.domain.RecipeResponse
import org.munchies.util.BadRequestException
import org.munchies.mapper.toDomain
import org.munchies.repository.RecipeRepository
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files

@Service
class RecipeImageService(
    private val s3Service: S3Service,
    private val recipeRepository: RecipeRepository
) {

    @Transactional
    suspend fun uploadImage(userId: String, id: Long, image: FilePart): RecipeResponse {
        // check if the recipe exists
        val recipe = recipeRepository.findByIdAndUserIdOrThrow(id, userId)

        // 1. if there is already an image saved, return BadRequestException
        if (recipe.filename != null) throw BadRequestException()

        // transfer the image to a temporary directory
        val filePath = withContext(Dispatchers.IO) {
            Files.createTempFile("temp", image.filename())
        }
        image.transferTo(filePath).awaitSingleOrNull()

        // 2. upload the image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.RECIPE, filePath)

        // 3. update the recipe with the filename and return it
        return recipeRepository
            .save(recipe.copy(filename = filename))
            .toDomain()
    }

    suspend fun downloadImage(userId: String, id: Long): Pair<ByteArray,String> {
        // check if the recipe and the image are existing
        val filename = recipeRepository
            .findByIdAndUserIdOrThrow(id, userId)
            .filename
            ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service
            .downloadFile(S3Bucket.RECIPE, filename)
            .readBytes() to filename
    }

    @Transactional
    suspend fun deleteImage(userId: String, id: Long) {
        // check if the recipe and the image are existing
        val recipe = recipeRepository.findByIdAndUserIdOrThrow(id, userId)
        val filename = recipe.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(S3Bucket.RECIPE, filename)

        // update the entity
        recipeRepository.save(recipe.copy(filename = null))
    }
}
