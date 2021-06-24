package me.yaman.can.demo.config


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.CustomConversions
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver
import org.springframework.data.r2dbc.dialect.R2dbcDialect
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import reactor.kotlin.core.publisher.toMono
import java.util.ArrayList

@Configuration
@EnableR2dbcAuditing
class DatabaseConfig(
    private val databaseClient: DatabaseClient
) : R2dbcDataAutoConfiguration(databaseClient) {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun auditorAware(): ReactiveAuditorAware<String>? {
        return ReactiveAuditorAware<String> {
            ReactiveSecurityContextHolder.getContext()
                .map { it.authentication }
                .filter { it.isAuthenticated }
                .map {
                    when (val userPrincipal = it.principal) {
                        is DefaultOAuth2User -> userPrincipal.name
                        is User -> userPrincipal.username
                        else -> "nobody"
                    }
                }
                .switchIfEmpty("unauthenticated".toMono())
                .onErrorResume { "noContext".toMono() }
        }
    }

    fun customConverters(dialect: String): MutableList<Converter<*, *>?> {
        log.info("Custom converter dialect is {}", dialect)
        val customConverters: MutableList<Converter<*, *>?> = ArrayList()
        when(dialect){
            "H2Dialect" -> {

            }
            "PostgresDialect" ->{

            }
        }
        return customConverters
    }

    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions? {
        val dialect: R2dbcDialect = DialectResolver.getDialect(databaseClient.connectionFactory)
        val converters: MutableList<Any?> = ArrayList(dialect.converters)
        converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS)
        return R2dbcCustomConversions(
            CustomConversions.StoreConversions.of(dialect.simpleTypeHolder, converters),
            customConverters(dialect.javaClass.simpleName)
        )
    }
}
