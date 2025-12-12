package org.munchies.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import java.io.InputStream
import java.util.UUID

/**
 * Manages the connection to the S3-storage.
 *
 * @param s3Client the client as an [S3Client].
 */
@Service
class S3Service(private val s3Client: S3Client) {

    /**
     * Uploads a file. The filename will be created with a random UUID and the extension from the assigned filename.
     * If no filename is provided, no extension will set. The final filename will be returned and should be used to associate the file.
     *
     * @param bucket the bucket where to store as an [S3Bucket].
     * @param bytes the bytes of the file as a [ByteArray].
     * @param filename an optional filename as a [String].
     * @return returns the filename as a [String].
     */
    fun uploadFile(bucket: S3Bucket, bytes: ByteArray, filename: String?): String {
        // when a filename was provided, extract the extension, otherwise it will be empty
        val extension = filename
            .orEmpty()
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        // build the filename with a random UUID and the extension
        val filename = "${UUID.randomUUID()}$extension"

        // upload the file with the filename to the desired bucket
        s3Client.putObject(
            { it.bucket(bucket.bucketName).key(filename).build() },
            RequestBody.fromBytes(bytes)
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
    fun downloadFile(bucket: S3Bucket, filename: String): InputStream = s3Client
        .getObject { it.bucket(bucket.bucketName).key(filename).build() }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     */
    fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }
}
