package org.munchies.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BadRequestException : ResponseStatusException(HttpStatus.BAD_REQUEST)

/**
 * If this object is `false`, then it throws [BadRequestException].
 */
fun Boolean.orThrowBadRequest() {
    if (!this) throw BadRequestException()
}

/**
 * Returns this object if it's not `null` or throws [BadRequestException] otherwise.
 */
fun <T> T?.orThrowBadRequest(): T = this ?: throw BadRequestException()
