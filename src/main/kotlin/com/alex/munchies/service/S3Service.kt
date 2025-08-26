package com.alex.munchies.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import java.io.InputStream
import java.util.UUID

@Service
class S3Service(private val s3Client: S3Client) {

    fun uploadFile(bucket: S3Bucket, bytes: ByteArray, filename: String?): String {
        val extension = filename
            .orEmpty()
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        s3Client.putObject(
            { it.bucket(bucket.bucketName).key(filename).build() },
            RequestBody.fromBytes(bytes)
        )

        return filename
    }

    fun downloadFile(bucket: S3Bucket, filename: String): InputStream {
        return s3Client.getObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }

    fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }
}
