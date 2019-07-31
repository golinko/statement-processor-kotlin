package com.golinko.statement.processor.web

import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementDTO
import com.golinko.statement.processor.dto.StatementValidationDTO
import com.golinko.statement.processor.service.StatementValidationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping(path = ["/demo"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@Api(value = "Demo Validation API")
class StatementDemoController(private val statementValidationService: StatementValidationService) {

    @ApiOperation(value = "Validate demo data by mimeType", response = Set::class)
    @GetMapping("/{mimeType}")
    fun validateDemoStatements(@PathVariable mimeType: MimeType): ResponseEntity<Set<StatementValidationDTO>> {
        log.debug("getStatements($mimeType)")

        val demoStatements = getDemoStatements(mimeType)
        val result = statementValidationService.validate(demoStatements)

        return ResponseEntity(result, if (CollectionUtils.isEmpty(result)) HttpStatus.OK else HttpStatus.BAD_REQUEST)
    }

    private fun getDemoStatements(mimeType: MimeType): List<StatementDTO> {
        val statementReader = statementValidationService.getStatementReader(mimeType)
        return statementReader.read(getDemoReader(statementReader.demoData))
    }

    private fun getDemoReader(data: Resource): Reader = Files.newBufferedReader(Paths.get(data.uri))

}
