package com.alex.munchies.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Suppress("unused")
@Configuration
@EnableWebSecurity
@Profile(SpringProfile.DEVELOPMENT)
class SecurityConfiguration(
    @Value("\${values.swagger.doc}") val swaggerDoc: String,
    @Value("\${values.swagger.ui}") val swaggerUi: String,
    @Value("\${values.swagger.ui-all}") val swaggerUiAll: String
) {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity {
            cors { disable() }
            csrf { disable() }
            authorizeHttpRequests {
                // swagger paths are available for everyone
                authorize(swaggerDoc, permitAll)
                authorize(swaggerUi, permitAll)
                authorize(swaggerUiAll, permitAll)

                // other routes are only accessible with authentication
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer { jwt { Customizer.withDefaults<HttpSecurity>() } }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
        }
        return httpSecurity.build()
    }
}
