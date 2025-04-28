package com.alex.munchies.repository

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserService {

    val userId: String
        get() {
            return SecurityContextHolder.getContext().authentication.name
        }
}