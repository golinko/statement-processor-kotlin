package com.golinko.statement.processor.service

import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementDTO
import com.golinko.statement.processor.dto.StatementValidationDTO
import com.golinko.statement.processor.service.reader.StatementReader
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import javax.validation.Validation
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Service
class StatementValidationService(val statementReaders: List<StatementReader>) {

    fun readStatements(inputStream: InputStream, mimeType: MimeType): List<StatementDTO> {
        log.debug("readStatements of mimeType: $mimeType")
        return getStatementReader(mimeType).read(InputStreamReader(inputStream))
    }

    fun validate(statements: List<StatementDTO>): Set<StatementValidationDTO> {
        log.debug("validate($statements)")

        val result = mutableSetOf<StatementValidationDTO>()

        val validator = Validation.buildDefaultValidatorFactory().validator

        statements
                .groupBy { it.reference }
                .forEach { (reference, list) ->
                    val dto = StatementValidationDTO(reference)
                    validateUniqueness(list, dto)
                    list.forEach { validateStatement(it, dto, validator) }

                    if (dto.errors.isNotEmpty()) {
                        result.add(dto)
                    }
                }

        return result
    }

    fun getStatementReader(mimeType: MimeType): StatementReader {
        return statementReaders.first { s -> s.supports(mimeType) }
    }

    private fun validateUniqueness(list: List<StatementDTO>, dto: StatementValidationDTO) {
        if (list.size > 1) {
            dto.errors
                    .getOrPut("reference") { ArrayList() }
                    .add("not unique")
        }
    }

    private fun validateStatement(statement: StatementDTO, dto: StatementValidationDTO, validator: Validator) {
        validator.validate(statement).forEach {
            dto.errors
                    .getOrPut(it.propertyPath.toString()) { ArrayList() }
                    .add(it.message)
        }
    }
}
