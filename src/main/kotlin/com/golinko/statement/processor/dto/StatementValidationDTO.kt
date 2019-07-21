package com.golinko.statement.processor.dto

class StatementValidationDTO(
        val reference: Long?,
        val errors: MutableMap<String, MutableList<String>> = mutableMapOf()
)
