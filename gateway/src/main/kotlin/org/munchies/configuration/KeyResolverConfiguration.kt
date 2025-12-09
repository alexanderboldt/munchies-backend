package org.munchies.configuration

import org.munchies.Header
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import reactor.core.publisher.Mono

@Suppress("unused")
@Configuration
class KeyResolverConfig {

    @Bean
    fun tokenKeyResolver(): KeyResolver =
        KeyResolver { exchange ->
            Mono.just(exchange
                .request
                .headers
                .getFirst(HttpHeaders.AUTHORIZATION)
                ?.removePrefix(Header.AUTHORIZATION_BEARER_PREFIX)
                ?: "anonymous"
            )
        }
}
