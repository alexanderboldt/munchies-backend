package org.munchies.controller

import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldHaveLength
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.munchies.Fixtures
import org.munchies.Header
import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.S3Bucket
import org.munchies.domain.FileResponse
import org.munchies.util.asFile

class FileControllerTest : BaseControllerTest() {

    // region create

    @Test
    fun `should not upload file and throw error with invalid bucket name`() {
        Given {
            multiPart(MultipartParam.FILE, Fixtures.image)
            header(Header.API_VERSION, "1")
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.FILES_BUCKET, "bucket")
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not upload file and throw bad-request with invalid multipart`() {
        Given {
            multiPart("image", Fixtures.image)
            header(Header.API_VERSION, "1")
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.FILES_BUCKET, S3Bucket.RECIPE.bucketName)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should upload a file`() {
        val file = Given {
            multiPart(MultipartParam.FILE, Fixtures.image)
            header(Header.API_VERSION, "1")
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.FILES_BUCKET, S3Bucket.RECIPE.bucketName)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asFile()
        }

        println(file)

        // verify the UUID and the extension
        file.filename.split(".").first() shouldHaveLength 36
        file.filename shouldEndWith Fixtures.image.extension
    }

    // endregion

    // region read

    @Test
    fun `should not download a file with invalid bucket name`() {
        // precondition: upload a file
        val file = uploadFile()

        val bytes = Given {
            header(Header.API_VERSION, "1")
        } When {
            get(Path.FILES_BUCKET_FILENAME, "bucket", file.filename)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        } Extract {
            asByteArray()
        }

        bytes.shouldNotBeNull()
        bytes.size shouldBeGreaterThan 0
    }

    @Test
    fun `should not download a file with invalid filename`() {
        // precondition: upload a file
        uploadFile()

        Given {
            header(Header.API_VERSION, "1")
        } When {
            get(Path.FILES_BUCKET_FILENAME, S3Bucket.RECIPE.bucketName, "invalid_filename.jpg")
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        } Extract {
            asByteArray()
        }
    }

    @Test
    fun `should download a file`() {
        // precondition: upload a file
        val file = uploadFile()

        val bytes = Given {
            header(Header.API_VERSION, "1")
        } When {
            get(Path.FILES_BUCKET_FILENAME, S3Bucket.RECIPE.bucketName, file.filename)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asByteArray()
        }

        bytes.shouldNotBeNull()
        bytes.size shouldBeGreaterThan 0
    }

    // endregion

    private fun uploadFile(): FileResponse {
        return Given {
            multiPart(MultipartParam.FILE, Fixtures.image)
            header(Header.API_VERSION, "1")
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.FILES_BUCKET, S3Bucket.RECIPE.bucketName)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asFile()
        }
    }
}
