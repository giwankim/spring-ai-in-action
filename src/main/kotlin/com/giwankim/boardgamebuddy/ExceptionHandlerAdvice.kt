package com.giwankim.boardgamebuddy

import org.springframework.context.MessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandlerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ProblemDetail {
        return ProblemDetail
            .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed")
            .apply {
                val validationMessages =
                    exception.bindingResult.allErrors.map(MessageSourceResolvable::getDefaultMessage)
                setProperty("errors", validationMessages)
                setProperty("timestamp", LocalDateTime.now())
            }
    }
}
