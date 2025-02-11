package com.alex.munchies.repository

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserService {

    fun getUserId(): String = SecurityContextHolder.getContext().authentication.name
}