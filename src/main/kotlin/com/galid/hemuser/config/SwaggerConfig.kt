package com.galid.hemuser.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestAttribute
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.DocExpansion
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun userApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("User V1")
            .apiInfo(ApiInfoBuilder().title("User API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/users/**"))
            .build()
            .ignoredParameterTypes(RequestAttribute::class.java)
    }

    @Bean
    fun tokenApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Token V1")
            .apiInfo(ApiInfoBuilder().title("Token API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/tokens/**"))
            .build()
            .ignoredParameterTypes(RequestAttribute::class.java)
    }

    @Bean
    fun friendApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Friends V1")
            .apiInfo(ApiInfoBuilder().title("Friend API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/friends/**"))
            .build()
            .ignoredParameterTypes(RequestAttribute::class.java)
    }

    @Bean
    fun swaggerUiConfiguration(): UiConfiguration? {
        return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build()
    }
}