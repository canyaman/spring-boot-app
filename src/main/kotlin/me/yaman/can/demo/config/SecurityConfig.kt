package me.yaman.can.demo.config

import me.yaman.can.demo.controller.TimeController
import org.springdoc.core.SpringDocConfigProperties
import org.springdoc.core.SwaggerUiConfigProperties
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
import org.springframework.boot.actuate.context.ShutdownEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Profile("oauth2")
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        swaggerConfig: SwaggerUiConfigProperties,
        springDocConfig: SpringDocConfigProperties,
        asciidocConfig: AsciidocConfig
    ): SecurityWebFilterChain {
        return http
            .authorizeExchange()
            .pathMatchers(
                swaggerConfig.path,
                swaggerConfig.configUrl ?: "${springDocConfig.apiDocs.path}/${org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE}",
                springDocConfig.apiDocs.path,
                springDocConfig.apiDocs.path + "/**",
                springDocConfig.webjars.prefix + "/**"
            ).permitAll()
            .pathMatchers(
                asciidocConfig.path
            ).permitAll()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(TimeController.path).permitAll()
            .matchers(EndpointRequest.toAnyEndpoint().excluding(ShutdownEndpoint::class.java)).permitAll()
            .anyExchange().authenticated()
            .and()
            .csrf().disable()
            .oauth2ResourceServer { oauth2 -> oauth2.jwt() }
            .build()
    }
}
