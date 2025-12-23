package org.munchies.controller

import kotlinx.coroutines.flow.Flow
import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.S3Bucket
import org.munchies.service.S3Service
import org.munchies.util.orThrowBadRequest
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
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(private val s3Service: S3Service) {

    @PostMapping(
        Path.FILES_BUCKET,
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        version = "1"
    )
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun upload(
        @PathVariable(PathParam.BUCKET) bucket: String,
        @RequestPart(MultipartParam.FILE) file: FilePart
    ) = s3Service.uploadFile(S3Bucket.fromBucketName(bucket).orThrowBadRequest(), file)

    @GetMapping(
        Path.FILES_BUCKET_FILENAME,
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE],
        version = "1"
    ) suspend fun download(
        @PathVariable(PathParam.BUCKET) bucket: String,
        @PathVariable(PathParam.FILENAME) filename: String
    ): ResponseEntity<Flow<DataBuffer>> {
        val data = s3Service.downloadFile(S3Bucket.fromBucketName(bucket).orThrowBadRequest(), filename)

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .body(data)
    }

    @DeleteMapping(
        Path.FILES_BUCKET_FILENAME,
        version = "1"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(
        @PathVariable(PathParam.BUCKET) bucket: String,
        @PathVariable(PathParam.FILENAME) filename: String
    ) = s3Service.deleteFile(S3Bucket.fromBucketName(bucket).orThrowBadRequest(), filename)
}
