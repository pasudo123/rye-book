package com.github.ryebook.common.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomSwaggerConfiguration {

    @Bean
    fun groupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("ryebook group")
            .pathsToMatch("/**")
            .addOpenApiCustomiser {
                it.info(
                    Info().title("ryebook API")
                        .description("ryebook api 입니다.")
                        .version("1.0.0")
                        .license(License().name("Apache 2.0").url("http://springdoc.org"))
                )
            }
            .build()
    }
}
