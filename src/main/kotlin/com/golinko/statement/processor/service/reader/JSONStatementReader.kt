package com.golinko.statement.processor.service.reader

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementDTO
import com.golinko.statement.processor.exceptions.StatementProcessorException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.Reader

private val log = KotlinLogging.logger {}

@Component
class JSONStatementReader(
        @Value("\${demo.json:classpath:records.json}")
        override val demoData: Resource,

        @Qualifier("objectMapper")
        private val objectMapper: ObjectMapper) : StatementReader {

    override fun read(reader: Reader): List<StatementDTO> {
        log.debug("read()")
        try {
            return objectMapper.readValue(reader, object : TypeReference<List<StatementDTO>>() {})
        } catch (e: Exception) {
            log.error("Error during json file read")
            throw StatementProcessorException("Error during json file read")
        }
    }

    override fun supports(mimeType: MimeType) = MimeType.JSON == mimeType
}
