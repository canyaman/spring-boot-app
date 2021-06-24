package me.yaman.can.demo.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.SwaggerUiOAuthProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
@OpenAPIDefinition()
class OpenApiConfig {

    @Configuration
    @ConfigurationProperties("springdoc.o-auth-flow")
    class OAuthFlowProperties : OAuthFlow() {
        var issuerUri: String? = null
    }

    @Bean
    fun swaggerConfigure(
        @Value("\${spring.application.name}") applicationName: String,
        buildProperties: BuildProperties,
        gitProperties: GitProperties,
        asciidocDocumentation: ExternalDocumentation?,
        securityScheme: SecurityScheme?,
        swaggerUiOAuthProperties: SwaggerUiOAuthProperties?,
        securityRequirements: List<SecurityRequirement>?
        ): OpenAPI {
        val openApi = OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title(applicationName)
                    .version(buildProperties.version)
                    .description("**${buildProperties.group}:${buildProperties.name}** GIT ${gitProperties.branch}:${gitProperties.shortCommitId} @ ${gitProperties.commitTime}")
            ).externalDocs(asciidocDocumentation)
        securityScheme?.let { openApi.components.addSecuritySchemes(securityName, securityScheme) }
        openApi.security(securityRequirements)
        return openApi
    }

    @Bean
    @Profile("oauth2")
    fun oauth2SecurityRequirement(): SecurityRequirement {
        return SecurityRequirement().addList(securityName)
    }
    @Bean
    @Profile("oauth2")
    fun oauthSecurityScheme(oAuthFlowProperties: OAuthFlowProperties): SecurityScheme {
        val oauth = SecurityScheme().type(SecurityScheme.Type.OAUTH2)
            .name("keycloak")
            .flows(
                OAuthFlows()
                    .authorizationCode(
                        OAuthFlow()
                            .authorizationUrl(oAuthFlowProperties.authorizationUrl ?: "${oAuthFlowProperties.issuerUri}/protocol/openid-connect/auth")
                            .tokenUrl(oAuthFlowProperties.tokenUrl ?: "${oAuthFlowProperties.issuerUri}/protocol/openid-connect/token")
                            .refreshUrl(oAuthFlowProperties.refreshUrl ?: "${oAuthFlowProperties.issuerUri}/protocol/openid-connect/token")
                            .scopes(oAuthFlowProperties.scopes)
                            .extensions(oAuthFlowProperties.extensions)
                    )
            )
        return oauth
    }

    companion object {
        const val securityName = "oauth2"
    }
}
