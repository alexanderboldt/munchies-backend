package org.munchies.controller

import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.service.S3Bucket
import org.munchies.service.S3Service
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(private val s3Service: S3Service) {

    @PostMapping(
        Path.FILES_BUCKET,
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        version = "1"
    ) suspend fun upload(
        @PathVariable(PathParam.BUCKET) bucket: String,
        @RequestPart(MultipartParam.FILE) file: FilePart
    ) = s3Service.uploadFile(S3Bucket.fromBucketName(bucket), file)
}
