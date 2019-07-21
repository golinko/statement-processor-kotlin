package com.golinko.statement.processor.web

import com.golinko.statement.processor.exceptions.ExceptionResponseMessage
import com.golinko.statement.processor.exceptions.StatementProcessorException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

private val log = KotlinLogging.logger {}

@ControllerAdvice
class ResponseExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ExceptionResponseMessage {
        log.warn("HTTP method argument type mismatch: {}", e.message)
        return ExceptionResponseMessage("The request is invalid.")
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ExceptionResponseMessage {
        log.warn("HTTP request method not supported: {}", e.message)
        return ExceptionResponseMessage("Request method used is not allowed for this endpoint.")
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StatementProcessorException::class)
    fun handleStatementProcessorException(e: StatementProcessorException): ExceptionResponseMessage {
        return ExceptionResponseMessage(e.message)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(e: Throwable): ExceptionResponseMessage {
        log.error(e.message, e)
        return ExceptionResponseMessage("Something went wrong while processing the request.")
    }
}
