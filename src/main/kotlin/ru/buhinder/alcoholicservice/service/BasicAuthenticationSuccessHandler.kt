package ru.buhinder.alcoholicservice.service

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class BasicAuthenticationSuccessHandler(
    private val tokenService: TokenService,
) : ServerAuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        val exchange: ServerWebExchange = webFilterExchange.exchange

        exchange.response
            .headers
            .add(AUTHORIZATION, tokenService.createToken(authentication.name))

        return webFilterExchange.chain.filter(exchange)
    }

}
