package com.alex.munchies.configuration

import com.alex.munchies.util.Path
import com.alex.munchies.util.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Suppress("unused")
@Configuration
@EnableWebSecurity
@Profile(SpringProfile.STAGE, SpringProfile.DEVELOPMENT)
class SecurityConfiguration(
    @param:Value($$"${values.swagger.doc}") val swaggerDoc: String,
    @param:Value($$"${values.swagger.ui}") val swaggerUi: String,
    @param:Value($$"${values.swagger.ui-all}") val swaggerUiAll: String
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
                authorize(HttpMethod.DELETE, Path.RECIPE, hasRole(Role.ADMIN))
                authorize(anyRequest, hasRole(Role.USER))
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = jwtAuthenticationConverter()
                }
            }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
        }
        return httpSecurity.build()
    }

    fun jwtAuthenticationConverter(): Converter<Jwt, AbstractAuthenticationToken> {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = (jwt.claims["realm_access"] as? Map<*, *>)?.get("roles") as? List<*>
            roles?.map { SimpleGrantedAuthority("ROLE_$it") } ?: emptyList()
        }
        return converter
    }
}
