package com.alex.munchies.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BadRequestException : ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input!")
