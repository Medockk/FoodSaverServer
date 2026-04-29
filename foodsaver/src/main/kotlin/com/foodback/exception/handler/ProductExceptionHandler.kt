package com.foodback.exception.handler

import com.foodback.exception.general.Error.GlobalErrorResponse
import com.foodback.exception.product.ProductNotFreshException
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice
class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFreshException::class)
    fun handleProductNotFreshException(exception: ProductNotFreshException): ResponseEntity<GlobalErrorResponse> {
        exception.printStackTrace()
        val body = GlobalErrorResponse(
            error = exception::class.java.simpleName,
            message = exception.message,
            httpCode = exception.getHttpStandardCode(),
            errorCode = exception.getCustomCode()
        )
        return ResponseEntity
            .status(body.httpCode)
            .body(body)
    }
}