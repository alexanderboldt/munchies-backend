package com.alex.munchies.service

import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.util.BadRequestException
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.RecipeRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class RecipeImageService(
    private val userService: UserService,
    private val s3Service: S3Service,
    private val recipeRepository: RecipeRepository
) {

    @Transactional
    fun uploadImage(id: Long, image: MultipartFile): RecipeResponse {
        // check if the recipe exists
        val recipeExisting = recipeRepository.findByIdAndUserIdOrThrow(id, userService.userId)

        // 1. if there is already an image saved, delete it first
        recipeExisting.filename?.let { s3Service.deleteFile(S3Bucket.RECIPE, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.RECIPE, image.bytes, image.originalFilename)

        // 3. update the recipe with the filename
        recipeExisting.filename = filename

        return recipeExisting.toDomain()
    }

    fun downloadImage(id: Long): Pair<ByteArray,String> {
        // check if the recipe and the image are existing
        val filename = recipeRepository
            .findByIdAndUserIdOrThrow(id, userService.userId)
            .filename
            ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service
            .downloadFile(S3Bucket.RECIPE, filename)
            .readBytes() to filename
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the recipe and the image are existing
        val recipeExisting = recipeRepository.findByIdAndUserIdOrThrow(id, userService.userId)
        val filename = recipeExisting.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(S3Bucket.RECIPE, filename)

        recipeExisting.filename = null
    }
}
