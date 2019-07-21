package com.golinko.statement.processor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())

    private fun metaData(): ApiInfo =
        ApiInfoBuilder()
                .title("StatementDTO Processor Service and REST API")
                .description("\"StatementDTO Processor Service and REST API for validating customer statement records\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/golinko/statement-processor/blob/master/LICENSE")
                .contact(Contact("Anna", "https://github.com/golinko/statement-processor", "noPersonalInfo@github.com"))
                .build()
}
