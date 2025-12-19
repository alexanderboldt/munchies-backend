package org.munchies.service

/**
 * Represents the buckets in the S3-storage.
 */
enum class S3Bucket(val bucketName: String) {
    RECIPE("recipe");

    companion object {
        fun fromBucketName(bucketName: String): S3Bucket {
            return entries.first { it.bucketName == bucketName }
        }
    }
}
