package org.munchies.controller

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
import org.munchies.util.asFile

class FileControllerTest : BaseControllerTest() {

    // region create

    @Test
    fun `should not upload file with invalid bucket name`() {
        Given {
            multiPart(MultipartParam.FILE, Fixtures.image)
            header(Header.API_VERSION, "1")
            contentType(ContentType.MULTIPART)
        } When {
            post(Path.FILES_BUCKET, "bucket")
        } Then {
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun `should not upload file with invalid multipart`() {
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
    fun `should upload file with valid bucket name`() {
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
}
