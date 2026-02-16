package org.munchies.configuration

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.Host
import aws.smithy.kotlin.runtime.net.Scheme
import aws.smithy.kotlin.runtime.net.url.Url
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "s3")
class S3Properties {
    lateinit var scheme: String
    lateinit var host: String
    var port: Int = 0
    lateinit var region: String
    lateinit var accessKeyId: String
    lateinit var secretAccessKey: String
}

@Configuration
class S3Configuration(private val s3: S3Properties) {

    @Bean
    fun s3Client() = S3Client {
        endpointUrl = Url {
            scheme = Scheme.parse(s3.scheme)
            host = Host.parse(s3.host)
            port = s3.port
        }
        region = s3.region
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = s3.accessKeyId
            secretAccessKey = s3.secretAccessKey
        }
        forcePathStyle = true
    }
}
