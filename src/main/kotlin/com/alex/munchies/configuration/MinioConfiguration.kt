package com.alex.munchies.configuration

import com.alex.munchies.service.MinioBucket
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfiguration(
    @param:Value($$"${minio.endpoint}") val endpoint: String,
    @param:Value($$"${minio.access-key}") val accessKey: String,
    @param:Value($$"${minio.secret-key}") val secretKey: String
) {
    private val minioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

    @Bean
    fun minioClient() = minioClient

    @PostConstruct
    fun init() {
        MinioBucket.entries.forEach {
            createBucket(it)
        }
    }

    private fun createBucket(bucket: MinioBucket) {
        val bucketExists = minioClient.bucketExists(
            BucketExistsArgs
                .builder()
                .bucket(bucket.bucketName)
                .build()
        )
        if (!bucketExists) {
            minioClient.makeBucket(
                MakeBucketArgs
                    .builder()
                    .bucket(bucket.bucketName)
                    .build()
            )
        }
    }
}
