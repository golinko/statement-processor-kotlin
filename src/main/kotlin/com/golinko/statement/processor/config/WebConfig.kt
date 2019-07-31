package com.golinko.statement.processor.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper =
            Jackson2ObjectMapperBuilder.json()
                    .modules(ParameterNamesModule(), Jdk8Module(), JavaTimeModule(), KotlinModule())
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .featuresToEnable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
                    .build()

    @Bean
    fun csvMapper() =
            CsvMapper().registerKotlinModule() as CsvMapper

    @Bean
    fun xmlMapper(): XmlMapper =
            Jackson2ObjectMapperBuilder.xml()
                    .modules(ParameterNamesModule(), Jdk8Module(), JavaTimeModule(), KotlinModule())
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .featuresToEnable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
                    .build()
}
