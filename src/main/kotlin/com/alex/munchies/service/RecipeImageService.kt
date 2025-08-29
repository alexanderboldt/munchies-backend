package com.alex.munchies.service

import com.alex.munchies.domain.Recipe
import com.alex.munchies.exception.RecipeNotFoundException
import com.alex.munchies.mapper.toDomain
import com.alex.munchies.repository.recipe.RecipeRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class RecipeImageService(
    private val userService: UserService,
    private val s3Service: S3Service,
    private val recipeRepository: RecipeRepository
) {

    fun uploadImage(id: Long, image: MultipartFile): Recipe {
        // check if the recipe exists
        val recipeExisting = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()

        // 1. if there is already an image saved, delete it first
        recipeExisting.filename?.let { s3Service.deleteFile(S3Bucket.RECIPE, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.RECIPE, image.bytes, image.originalFilename)

        // 3. update the artist with the filename
        return recipeRepository.save(recipeExisting.copy(filename = filename)).toDomain()
    }

    fun downloadImage(id: Long): Pair<ByteArray,String> {
        // check if the recipe and the image are existing
        val filename = recipeRepository.findByIdAndUserId(id, userService.userId)?.filename ?: throw RecipeNotFoundException()

        // download the file and return it with the filename
        return s3Service
            .downloadFile(S3Bucket.RECIPE, filename)
            .readBytes() to filename
    }

    fun deleteImage(id: Long) {
        // check if the recipe and the image are existing
        val recipeExisting = recipeRepository.findByIdAndUserId(id, userService.userId) ?: throw RecipeNotFoundException()
        val filename = recipeExisting.filename ?: throw RecipeNotFoundException()

        // delete the file
        s3Service.deleteFile(S3Bucket.RECIPE, filename)

        // update the recipe by deleting the filename
        recipeRepository.save(recipeExisting.copy(filename = null))
    }
}
