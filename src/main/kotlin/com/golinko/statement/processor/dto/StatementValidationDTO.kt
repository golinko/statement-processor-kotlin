package com.golinko.statement.processor.dto

data class StatementValidationDTO(
        val reference: Long,
        val errors: MutableMap<String, MutableList<String>> = mutableMapOf()
)
