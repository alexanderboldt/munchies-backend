package com.alex.munchies.configuration

import com.alex.munchies.service.S3Bucket
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Suppress("unused")
@Configuration
class S3Configuration(
    @param:Value($$"${spring.s3.endpoint-override}") private val endpoint: String,
    @param:Value($$"${spring.s3.aws.region}") private val region: String,
    @param:Value($$"${spring.s3.aws.credentials.static-provider.access-key-id}") private val accessKey: String,
    @param:Value($$"${spring.s3.aws.credentials.static-provider.secret-access-key}") private val secretKey: String
) {

    private val s3Client = S3Client.builder()
        .endpointOverride(URI.create(endpoint))
        .region(Region.of(region))
        .forcePathStyle(true)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        ).build()

    @Bean
    fun s3Client(): S3Client = s3Client

    @PostConstruct
    fun init() {
        // try to create all buckets, if they don't exist
        S3Bucket.entries.forEach {
            createBucket(it)
        }
    }

    private fun createBucket(bucket: S3Bucket) {
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .buckets()
            .any { it.name() == bucket.bucketName }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket.bucketName).build() }
        }
    }
}
