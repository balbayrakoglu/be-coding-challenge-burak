package de.dkb.api.codeChallenge.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.servlet.http.HttpServletRequest
import org.springframework.validation.FieldError
import java.time.LocalDateTime


@RestControllerAdvice
class GlobalRestExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalRestExceptionHandler::class.java)

    data class DefaultErrorResponse(
        val timestamp: String,
        val status: Int,
        val error: String,
        val path: String
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<DefaultErrorResponse> {
        val errorMessage = ex.bindingResult.allErrors
            .joinToString("; ") { (it as FieldError).defaultMessage ?: "Validation failed" }

        val body = DefaultErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = errorMessage,
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, request: HttpServletRequest): ResponseEntity<DefaultErrorResponse> {
        val body = DefaultErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = ex.message ?: "Bad Request",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException, request: HttpServletRequest): ResponseEntity<DefaultErrorResponse> {
        val body = DefaultErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            status = HttpStatus.NOT_FOUND.value(),
            error = ex.message ?: "Not Found",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: HttpServletRequest): ResponseEntity<DefaultErrorResponse> {
        log.error("Unhandled exception occurred", ex)
        val body = DefaultErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}