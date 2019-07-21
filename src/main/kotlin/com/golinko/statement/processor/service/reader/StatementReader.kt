package com.golinko.statement.processor.service.reader

import com.golinko.statement.processor.dto.MimeType
import com.golinko.statement.processor.dto.StatementDTO
import org.springframework.core.io.Resource
import java.io.Reader

interface StatementReader {
    val demoData: Resource

    fun read(reader: Reader): List<StatementDTO>

    fun supports(mimeType: MimeType): Boolean
}
