package com.foodback.demo.exception.handler

import com.foodback.demo.exception.general.GlobalErrorResponse
import com.foodback.demo.exception.product.ProductNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(ex: ProductNotFoundException): ResponseEntity<GlobalErrorResponse> {
        val error = GlobalErrorResponse(
            error = "Product Not Found",
            message = ex.message ?: ex.localizedMessage ?: "",
            code = 400
        )

        return ResponseEntity.status(400)
            .body(error)
    }
}