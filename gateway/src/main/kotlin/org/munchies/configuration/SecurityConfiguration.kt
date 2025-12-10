package org.munchies.configuration

import org.munchies.Path
import org.munchies.util.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

@Suppress("unused")
@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(httpSecurity: ServerHttpSecurity) = httpSecurity {
        cors { disable() }
        csrf { disable() }
        authorizeExchange {
            // the login-route is for everybody
            authorize(Path.AUTH_LOGIN, permitAll)

            // deleting all recipes route is just for admin-role
            authorize(pathMatchers(HttpMethod.DELETE, Path.RECIPES), hasRole(Role.ADMIN))

            // all remaining routes are for user-roles
            authorize(anyExchange, hasRole(Role.USER))
        }
        oauth2ResourceServer {
            jwt {
                jwtAuthenticationConverter = ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter())
            }
        }
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
