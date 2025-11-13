package com.alex.munchies.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke

@Suppress("unused")
@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(httpSecurity: ServerHttpSecurity) = httpSecurity {
        cors { disable() }
        csrf { disable() }
        authorizeExchange {
            authorize(anyExchange, authenticated)
        }
        oauth2ResourceServer {
            jwt {}
        }
    }
}