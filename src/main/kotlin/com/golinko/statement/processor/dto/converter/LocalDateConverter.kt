package com.golinko.statement.processor.dto.converter

import com.fasterxml.jackson.databind.util.StdConverter
import mu.KotlinLogging
import org.springframework.lang.Nullable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

class LocalDateConverter : StdConverter<String, LocalDate>() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun convert(@Nullable source: String?): LocalDate? {
        log.debug("convert($source)")
        val result = source?.let {
            LocalDate.parse(it, formatter)
        }
        log.debug("result: $result")
        return result
    }
}
