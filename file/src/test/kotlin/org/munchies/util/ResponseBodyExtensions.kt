package org.munchies.util

import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions
import org.munchies.domain.FileResponse

fun ResponseBodyExtractionOptions.asFile(): FileResponse = `as`(object : TypeRef<FileResponse>() {})
