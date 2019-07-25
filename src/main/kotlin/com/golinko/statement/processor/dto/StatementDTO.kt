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
class StatementDTO {
    val reference: Long? = null
    val accountNumber: String? = null
    val description: String? = null

    @PositiveOrZero
    val startBalance: BigDecimal? = null

    val mutation: BigDecimal? = null

    @PositiveOrZero
    val endBalance: BigDecimal? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(converter = LocalDateConverter::class)
    @PastOrPresent
    val date: LocalDate? = null

    @AssertTrue(message = "not equals to sum of start balance and mutation")
    private fun isEndBalance(): Boolean = startBalance?.add(mutation)?.compareTo(endBalance) == 0
}
