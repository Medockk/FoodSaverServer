package com.foodback.exception.handler

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.Error.GlobalErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.io.EOFException
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * Rest controller advice to handle general error like server error
 */
@Order(1)
@RestControllerAdvice
class GlobalExceptionHandler : HandlerExceptionResolver {

    /**
     * Handle [RuntimeException]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException): ResponseEntity<GlobalErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = exception.message ?: "Runtime Exception",
                    message = exception.localizedMessage ?: "Oops... Unknown error",
                    httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )
    }

    /**
     * Handle all [Exception]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(exception: Exception): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = "Internal server error",
                    message = exception.message ?: "Unknown exception...",
                    httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )
    }

    /**
     * Handle [IOException]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(IOException::class)
    fun handleIOException(exception: IOException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(
                GlobalErrorResponse(
                    error = "Network Exception",
                    message = "Network error when accessing a remote service: ${exception.message}",
                    httpCode = HttpStatus.BAD_GATEWAY.value()
                )
            )
    }

    /**
     * Handle [SocketException]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(SocketException::class)
    fun handleSocketException(exception: SocketException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(
                GlobalErrorResponse(
                    error = "Network Exception",
                    message = "Network error when accessing a remote service: ${exception.message}",
                    httpCode = HttpStatus.BAD_GATEWAY.value()
                )
            )
    }

    /**
     * Handle [TimeoutException]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(TimeoutException::class)
    fun handleTimeoutException(exception: TimeoutException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.GATEWAY_TIMEOUT)
            .body(
                GlobalErrorResponse(
                    error = "Timeout Exception",
                    message = "Oops...server throw timeout exception with message: ${exception.message}",
                    httpCode = HttpStatus.GATEWAY_TIMEOUT.value()
                )
            )
    }

    /**
     * Handle [EOFException]
     * @return [GlobalErrorResponse] - Response of error type
     */
    @ExceptionHandler(EOFException::class)
    fun handleEOFException(exception: EOFException): ResponseEntity<GlobalErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                GlobalErrorResponse(
                    error = "EOF Exception",
                    message = "Oops...server throw timeout exception with message: ${exception.message}",
                    httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgument(e: MethodArgumentNotValidException): ResponseEntity<GlobalErrorResponse> {
        e.printStackTrace()
        val error = e.bindingResult.allErrors.joinToString(separator = "; ") {
            "${it.defaultMessage}"
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                GlobalErrorResponse(
                    error = "Method argument exception",
                    message = error,
                    httpCode = HttpStatus.BAD_REQUEST.value()
                )
            )
    }

    @ExceptionHandler(GlobalError::class)
    fun handleGlobalError(e: GlobalError): ResponseEntity<GlobalErrorResponse> {
        e.printStackTrace()

        val httpStatus = e.httpStatus

        return ResponseEntity
            .status(httpStatus)
            .body(
                GlobalErrorResponse(
                    error = e.javaClass.simpleName,
                    message = e.message.ifBlank { e.customCode.toString() },
                    httpCode = e.getHttpStandardCode(),
                    errorCode = e.getCustomCode()
                )
            )
    }

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: java.lang.Exception
    ): ModelAndView? {
        return null
    }
}