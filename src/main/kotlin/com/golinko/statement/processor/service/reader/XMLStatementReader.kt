package com.golinko.statement.processor.service.reader

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementDTO
import com.golinko.statement.processor.exceptions.StatementProcessorException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.Reader

private val log = KotlinLogging.logger {}

@Component
class XMLStatementReader(
        @Value("\${demo.xml:classpath:records.xml}")
        override val demoData: Resource,
        private val xmlMapper: XmlMapper) : StatementReader {

    override fun read(reader: Reader): List<StatementDTO> {
        log.debug("read()")
        try {
            return xmlMapper
                    .readerFor(StatementDTO::class.java)
                    .readValues<StatementDTO>(reader)
                    .readAll()
        } catch (e: Exception) {
            log.error("Error during xml file read", e)
            throw StatementProcessorException("Error during xml file read")
        }
    }

    override fun supports(mimeType: MimeType) = MimeType.XML == mimeType
}
