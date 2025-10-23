package com.alex.munchies.controller

import com.alex.munchies.service.RecipeImageService
import com.alex.munchies.util.Path
import com.alex.munchies.util.RateLimit
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Suppress("unused")
@RateLimiter(name = RateLimit.BASIC)
@RestController
@RequestMapping(Path.RECIPE_IMAGE)
class RecipeImageController(private val recipeImageService: RecipeImageService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@PathVariable id: Long, @RequestParam image: MultipartFile) = recipeImageService.uploadImage(id, image)

    @GetMapping(produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun downloadImage(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val (bytes, filename) = recipeImageService.downloadImage(id)

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .body(bytes)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteImage(@PathVariable id: Long) = recipeImageService.deleteImage(id)
}
