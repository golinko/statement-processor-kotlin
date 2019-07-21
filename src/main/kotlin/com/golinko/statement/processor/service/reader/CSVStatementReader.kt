package com.golinko.statement.processor.service.reader

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
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
class CSVStatementReader(
        @Value("\${demo.csv:classpath:records.csv}")
        override val demoData: Resource) : StatementReader {

    override fun read(reader: Reader): List<StatementDTO> {
        log.debug("read()")
        val mapper = CsvMapper().enable(CsvParser.Feature.TRIM_SPACES)
        val bootstrapSchema = mapper
                .typedSchemaFor(StatementDTO::class.java)
                .withHeader()
                .withLineSeparator(",")
                .withNullValue("NULL")
        try {
            return mapper
                    .readerFor(StatementDTO::class.java)
                    .with(bootstrapSchema)
                    .readValues<StatementDTO>(reader)
                    .readAll()
        } catch (e: Exception) {
            log.error("Error during csv file read")
            throw StatementProcessorException("Error during csv file read")
        }
    }

    override fun supports(mimeType: MimeType) = MimeType.CSV == mimeType
}
