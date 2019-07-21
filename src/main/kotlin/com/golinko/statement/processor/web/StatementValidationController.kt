package com.golinko.statement.processor.web

import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementValidationDTO
import com.golinko.statement.processor.service.StatementValidationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping(path = ["/validate"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@Api(value = "Statement Validation API")
class StatementValidationController(val statementValidationService: StatementValidationService) {

    @ApiOperation(value = "Validate uploaded data by mimeType", response = Set::class)
    @PostMapping("/{mimeType}")
    fun validateStatements(
            @PathVariable mimeType: MimeType,
            @RequestBody dataFile: MultipartFile): ResponseEntity<Set<StatementValidationDTO>> {
        log.debug("validateStatements({}, {})", mimeType, dataFile.name)

        val statements = statementValidationService.readStatements(dataFile.inputStream, mimeType)
        val result = statementValidationService.validate(statements)

        return ResponseEntity(result, if (CollectionUtils.isEmpty(result)) HttpStatus.OK else HttpStatus.BAD_REQUEST)
    }
}
