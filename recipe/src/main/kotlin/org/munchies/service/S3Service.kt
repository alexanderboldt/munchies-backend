package org.munchies.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import java.io.InputStream
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.extension

/**
 * Manages the connection to the S3-storage.
 *
 * @param s3Client the client as an [S3Client].
 */
@Service
class S3Service(private val s3Client: S3Client) {

    /**
     * Uploads a file. The filename will be created with a random UUID and the extension from the assigned filename.
     * The final filename will be returned and should be used to associate the file.
     *
     * @param bucket the bucket where to store as an [S3Bucket].
     * @param filePath the path of the file as a [Path].
     * @return returns the filename as a [String].
     */
    suspend fun uploadFile(bucket: S3Bucket, filePath: Path): String {
        // build the filename with a random UUID and the extension
        val filename = "${UUID.randomUUID()}.${filePath.extension}"

        // upload the file with the filename to the desired bucket
        s3Client.putObject(
            { it.bucket(bucket.bucketName).key(filename).build() },
            RequestBody.fromFile(filePath)
        )

        return filename
    }

    /**
     * Downloads a file.
     *
     * @param bucket the bucket to download from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     * @return the result as an [InputStream].
     */
    suspend fun downloadFile(bucket: S3Bucket, filename: String): InputStream = s3Client
        .getObject { it.bucket(bucket.bucketName).key(filename).build() }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     */
    suspend fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }
}
