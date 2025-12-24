package org.munchies.client

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.munchies.Header
import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.S3Bucket
import org.munchies.domain.FileResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono

/**
 * Manages the connection to the file microservice.
 */
@Service
class FileClient(@param:Value($$"${services.file.url}") private val fileServiceUrl: String) {

    private val client = WebClient.create(fileServiceUrl)

    suspend fun upload(bucket: S3Bucket, file: FilePart): FileResponse {
        return client
            .post()
            .uri(Path.FILES_BUCKET, bucket.bucketName)
            .header(Header.API_VERSION, "1")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(MultipartParam.FILE, file))
            .retrieve()
            .bodyToMono<FileResponse>()
            .awaitSingle()
    }

    suspend fun download(bucket: S3Bucket, filename: String): Flow<DataBuffer> {
        return client
            .get()
            .uri(Path.FILES_BUCKET_FILENAME, bucket.bucketName, filename)
            .header(Header.API_VERSION, "1")
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .retrieve()
            .bodyToFlux<DataBuffer>()
            .asFlow()
    }

    suspend fun delete(bucket: S3Bucket, filename: String) {
        client
            .delete()
            .uri(Path.FILES_BUCKET_FILENAME, bucket.bucketName, filename)
            .header(Header.API_VERSION, "1")
            .retrieve()
            .bodyToMono<Any>()
            .awaitSingleOrNull()
    }
}
