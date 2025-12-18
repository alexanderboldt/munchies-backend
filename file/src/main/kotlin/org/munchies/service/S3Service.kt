package org.munchies.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.extension

/**
 * Manages the connection to the S3-storage.
 *
 * @param s3Client the client as an [software.amazon.awssdk.services.s3.S3AsyncClient].
 */
@Service
class S3Service(private val s3Client: S3AsyncClient) {

    /**
     * Creates a bucket if it not exists yet.
     *
     * @param bucket the bucket to create as a [S3Bucket].
     */
    suspend fun createBucketIfNotExists(bucket: S3Bucket) {
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .await()
            .buckets()
            .any { it.name() == bucket.bucketName }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket.bucketName).build() }.await()
        }
    }

    /**
     * Uploads a file. The filename will be created with a random UUID and the extension from the assigned filename.
     * The final filename will be returned and should be used to associate the file.
     *
     * @param bucket the bucket where to store as an [S3Bucket].
     * @param filePath the path of the file as a [java.nio.file.Path].
     * @return returns the filename as a [String].
     */
    suspend fun uploadFile(bucket: S3Bucket, filePath: Path): String {
        // build the filename with a random UUID and the extension
        val filename = "${UUID.randomUUID()}.${filePath.extension}"

        // upload the file with the filename to the desired bucket
        s3Client.putObject(
            { it.bucket(bucket.bucketName).key(filename).build() },
            AsyncRequestBody.fromFile(filePath)
        ).await()

        return filename
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

        return s3Client
            .getObject(
                GetObjectRequest.builder()
                    .bucket(bucket.bucketName)
                    .key(filename)
                    .build(),
                AsyncResponseTransformer.toPublisher()
            ).await()
            .asFlow()
            .map { factory.wrap(it) }
    }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     */
    suspend fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }.await()
    }
}
