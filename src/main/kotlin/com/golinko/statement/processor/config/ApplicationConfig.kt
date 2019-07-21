package com.golinko.statement.processor.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class ApplicationConfig {
    @Bean
    fun mappingJackson2HttpMessageConverter(objectMapper: ObjectMapper) = MappingJackson2HttpMessageConverter(objectMapper)
}
