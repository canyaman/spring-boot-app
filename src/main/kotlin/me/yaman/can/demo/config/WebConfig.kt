package me.yaman.can.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@Profile("cors")
class WebConfig {

    @Bean
    fun corsWebFilter(corsConfig: WebCorsConfiguration): CorsWebFilter? {
        corsConfig.validateAllowCredentials()
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }

    @Configuration
    @ConfigurationProperties("web.cors-configuration")
    class WebCorsConfiguration() : CorsConfiguration()
}
