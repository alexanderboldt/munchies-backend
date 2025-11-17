package com.alex.munchies.configuration

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
                .getFirst("Authorization")
                ?.removePrefix("Bearer ")
                ?: "anonymous"
            )
        }
}
