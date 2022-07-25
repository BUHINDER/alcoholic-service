package ru.buhinder.alcoholicservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.HTTP_BASIC
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import ru.buhinder.alcoholicservice.converter.JwtServerAuthenticationConverter
import ru.buhinder.alcoholicservice.service.AlcoholicDetailsService
import ru.buhinder.alcoholicservice.service.BasicAuthenticationSuccessHandler
import ru.buhinder.alcoholicservice.service.JwtAuthenticationManager


@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    companion object {
        const val jwtMatcher = "/api/alcoholic/**"
        const val httpMatcher = "/api/login"
    }

    @Bean
    fun http(
        http: ServerHttpSecurity,
        alcoholicDetailsService: AlcoholicDetailsService,
        successHandler: BasicAuthenticationSuccessHandler,
        jwtAuthManager: JwtAuthenticationManager,
        jwtServerAuthenticationConverter: JwtServerAuthenticationConverter,
    ): SecurityWebFilterChain? {
        return http
            .csrf().disable()
            .cors()

            .and()

            .authorizeExchange()
            //TODO refactor paths
            .pathMatchers("/api/refresh", "/api/register", "/api/logout")
            .permitAll()

            .and()

            .authorizeExchange()
            .pathMatchers(httpMatcher)
            .authenticated()
            .and()
            .addFilterAt(getBasicAuthenticationFilter(alcoholicDetailsService, successHandler), HTTP_BASIC)

            .authorizeExchange()
            .pathMatchers(jwtMatcher)
            .authenticated()
            .and()
            .addFilterAt(getJwtAuthenticationFilter(jwtAuthManager, jwtServerAuthenticationConverter), AUTHENTICATION)

            .build()
    }

    private fun getBasicAuthenticationFilter(
        alcoholicDetailsService: AlcoholicDetailsService,
        successHandler: BasicAuthenticationSuccessHandler,
    ): AuthenticationWebFilter {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(alcoholicDetailsService)
        val authenticationWebFilter = AuthenticationWebFilter(authenticationManager)
        authenticationWebFilter.setAuthenticationSuccessHandler(successHandler)
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(httpMatcher))

        return authenticationWebFilter
    }

    private fun getJwtAuthenticationFilter(
        jwtAuthenticationManager: JwtAuthenticationManager,
        jwtServerAuthenticationConverter: JwtServerAuthenticationConverter,
    ): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtServerAuthenticationConverter)
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(jwtMatcher))
        return authenticationWebFilter
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
