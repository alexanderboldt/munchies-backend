package org.munchies.controller

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import kotlinx.coroutines.flow.Flow
import org.munchies.Header
import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.service.RecipeImageService
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class RecipeImageController(private val recipeImageService: RecipeImageService) {

    @PostMapping(
        Path.RECIPES_IMAGES,
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        version = "1"
    ) suspend fun uploadImage(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long,
        @NotNull @RequestPart(MultipartParam.IMAGE) image: FilePart
    ) = recipeImageService.uploadImage(userId, id, image)

    @GetMapping(
        Path.RECIPES_IMAGES,
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE],
        version = "1"
    ) suspend fun downloadImage(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long
    ): ResponseEntity<Flow<DataBuffer>> {
        val (data, filename) = recipeImageService.downloadImage(userId, id)

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .body(data)
    }

    @DeleteMapping(Path.RECIPES_IMAGES, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteImage(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.RECIPE_ID) id: Long
    ) = recipeImageService.deleteImage(userId, id)
}
