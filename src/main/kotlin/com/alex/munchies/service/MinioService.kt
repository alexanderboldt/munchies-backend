package com.alex.munchies.service

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.UUID

@Service
class MinioService(private val minioClient: MinioClient) {

    fun uploadFile(bucket: MinioBucket, file: MultipartFile): String {
        val extension = file
            .originalFilename
            .orEmpty()
            .substringAfter(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucket.bucketName)
                .`object`(filename)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )

        return filename
    }

    fun downloadFile(bucket: MinioBucket, filename: String): InputStream {
        return minioClient.getObject(
            GetObjectArgs
                .builder()
                .bucket(bucket.bucketName)
                .`object`(filename)
                .build()
        )
    }

    fun deleteFile(bucket: MinioBucket, filename: String) {
        minioClient.removeObject(
            RemoveObjectArgs
                .builder()
                .bucket(bucket.bucketName)
                .`object`(filename)
                .build()
        )
    }
}
