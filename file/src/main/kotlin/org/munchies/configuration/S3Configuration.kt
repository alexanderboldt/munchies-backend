package org.munchies.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import java.net.URI

@Configuration
class S3Configuration(
    @param:Value($$"${spring.s3.endpoint-override}") private val endpoint: String,
    @param:Value($$"${spring.s3.aws.region}") private val region: String,
    @param:Value($$"${spring.s3.aws.credentials.static-provider.access-key-id}") private val accessKey: String,
    @param:Value($$"${spring.s3.aws.credentials.static-provider.secret-access-key}") private val secretKey: String
) {

    @Bean
    fun s3Client(): S3AsyncClient = S3AsyncClient.builder()
        .endpointOverride(URI.create(endpoint))
        .region(Region.of(region))
        .forcePathStyle(true)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        ).build()
}
