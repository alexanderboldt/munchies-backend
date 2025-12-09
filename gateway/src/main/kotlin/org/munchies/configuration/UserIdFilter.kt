package org.munchies.configuration

import org.munchies.Header
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Suppress("unused")
@Component
class UserIdFilter : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> = exchange
        .getPrincipal<JwtAuthenticationToken>()
        .flatMap { auth ->
            // build the new request with the added user-id
            val newRequest = exchange
                .request
                .mutate()
                .header(Header.USER_ID, auth.token.subject)
                .build()

            // build the exchange with the new request
            chain.filter(exchange.mutate().request(newRequest).build())
        }
}
