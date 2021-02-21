package me.yaman.can.demo.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition()
class OpenApiConfig {
    @Bean
    fun swaggerConfigure(
        @Value("\${spring.application.name}") applicationName: String,
        buildProperties: BuildProperties,
        gitProperties: GitProperties,
        asciidocDocumentation: ExternalDocumentation?
    ): OpenAPI {
        val openApi = OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title(applicationName)
                    .version(buildProperties.version)
                    .description("**${buildProperties.group}:${buildProperties.name}** GIT ${gitProperties.branch}:${gitProperties.shortCommitId} @ ${gitProperties.commitTime}")
            ).externalDocs(asciidocDocumentation)
        return openApi
    }
}
