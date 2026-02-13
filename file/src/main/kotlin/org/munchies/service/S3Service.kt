package org.munchies.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.createBucket
import aws.sdk.kotlin.services.s3.deleteObject
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.putObject
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.munchies.S3Bucket
import org.munchies.domain.FileResponse
import org.munchies.util.orThrowBadRequest
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.util.UUID
import kotlin.io.path.extension

/**
 * Manages the connection to the S3-storage.
 *
 * @param s3Client the client as an [aws.sdk.kotlin.services.s3].
 */
@Service
class S3Service(private val s3Client: S3Client) {

    /**
     * Will be called automatically and creates the buckets in the storage.
     */
    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            S3Bucket.entries.forEach { s3 -> createBucketIfNotExists(s3) }
        }
    }

    /**
     * Creates a bucket if it not exists yet.
     *
     * @param bucket the bucket to create as a [S3Bucket].
     */
    suspend fun createBucketIfNotExists(bucket: S3Bucket) {
        val bucketExists = s3Client
            .listBuckets()
            .buckets
            ?.any { it.name == bucket.bucketName }

        if (bucketExists == false) {
            s3Client.createBucket {
                this.bucket = bucket.bucketName
            }
        }
    }

    /**
     * Uploads a file. The filename will be created with a random UUID and the extension from the assigned filename.
     * The final filename will be returned and should be used to associate the file.
     *
     * @param bucket the bucket where to store as an [S3Bucket].
     * @param file the file as a [FilePart].
     * @return returns the response with the filename as a [FileResponse].
     */
    suspend fun uploadFile(bucket: S3Bucket, file: FilePart): FileResponse {
        // transfer the image to a temporary directory
        val filePath = withContext(Dispatchers.IO) {
            Files.createTempFile("temp", file.filename())
        }
        file.transferTo(filePath).awaitSingleOrNull()

        // build the filename with a random UUID and the extension
        val filename = "${UUID.randomUUID()}.${filePath.extension}"

        // upload the file with the filename to the desired bucket
        s3Client.putObject {
            this.bucket = bucket.bucketName
            this.key = filename
            this.body = filePath.asByteStream()
        }

        return FileResponse(filename)
    }

    /**
     * Downloads a file.
     *
     * @param bucket the bucket to download from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     * @return the result as a [org.springframework.core.io.buffer.DataBuffer] wrapped in a [kotlinx.coroutines.flow.Flow].
     */
    suspend fun downloadFile(bucket: S3Bucket, filename: String): Flow<DataBuffer> {
        val factory = DefaultDataBufferFactory()

        val request = GetObjectRequest {
            this.bucket = bucket.bucketName
            this.key = filename
        }

        return s3Client.getObject(request) { response ->
            flowOf(response.body?.toByteArray().orThrowBadRequest())
        }.map { factory.wrap(it) }
    }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     */
    suspend fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            this.bucket = bucket.bucketName
            key = filename
        }
    }
}
