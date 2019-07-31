package com.golinko.statement.processor.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.golinko.statement.processor.dto.converter.LocalDateConverter
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero

@JsonPropertyOrder("reference", "accountNumber", "description", "startBalance", "mutation", "endBalance", "date")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class StatementDTO(
        val reference: Long,
        val accountNumber: String,
        val description: String,

        @get:PositiveOrZero
        val startBalance: BigDecimal,

        val mutation: BigDecimal,

        @get:PositiveOrZero
        val endBalance: BigDecimal,

        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @get:JsonDeserialize(converter = LocalDateConverter::class)
        @get:PastOrPresent
        val date: LocalDate) {

    @AssertTrue(message = "not equals to sum of start balance and mutation")
    fun isEndBalance() = startBalance.add(mutation).compareTo(endBalance) == 0
}
