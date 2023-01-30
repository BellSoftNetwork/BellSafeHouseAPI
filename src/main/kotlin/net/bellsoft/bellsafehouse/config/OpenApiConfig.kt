package net.bellsoft.bellsafehouse.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    `in` = SecuritySchemeIn.HEADER,
)
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().apply {
                    title = "Bell Safe House API"
                    description = "Bell Safe House 공식 API 문서입니다"
                    version = "v0.0.1"
                },
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Bell Safe House API Git Repository")
                    .url("https://gitlab.bellsoft.net/connected-life/bell-safe-house-api.git"),
            )
    }
}
