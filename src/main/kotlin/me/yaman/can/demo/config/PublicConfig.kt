package me.yaman.can.demo.config

import me.yaman.can.demo.controller.TimeController
import org.springdoc.core.SpringDocConfigProperties
import org.springdoc.core.SwaggerUiConfigProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Profile("public")
@Configuration
class PublicConfig {
    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        swaggerConfig: SwaggerUiConfigProperties,
        springDocConfig: SpringDocConfigProperties
    ): SecurityWebFilterChain {
        return http
            .authorizeExchange()
            .pathMatchers("/**")
            .permitAll()
            .and()
            .csrf()
            .disable()
            .build()
    }
}