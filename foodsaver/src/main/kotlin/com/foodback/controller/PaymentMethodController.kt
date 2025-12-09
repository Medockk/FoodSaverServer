package com.foodback.controller

import com.foodback.dto.request.payment.PaymentRequestModel
import com.foodback.dto.response.payment.PaymentResponseModel
import com.foodback.exception.payment.PaymentMethodException
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.service.PaymentMethodService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/payment")
class PaymentMethodController(
    private val paymentMethodService: PaymentMethodService
) {

    @GetMapping("all")
    fun getAllPaymentMethod(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<List<PaymentResponseModel>> {
        val methods = paymentMethodService.getAllPaymentMethod(principal.uid)
        println(methods)
        return ResponseEntity
            .ok(methods)
    }

    @GetMapping("current")
    fun getCurrentPaymentMethod(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<PaymentResponseModel> {
        val paymentMethod = paymentMethodService.getCurrentPaymentMethod(principal.uid)
        return if (paymentMethod == null) {
            ResponseEntity.noContent()
                .build()
        } else {
            ResponseEntity.ok(paymentMethod)
        }
    }

    @PostMapping
    fun addPaymentMethod(
        @AuthenticationPrincipal
        principal: UserDetailsImpl,
        @RequestBody
        request: PaymentRequestModel
    ): ResponseEntity<PaymentResponseModel> {
        val paymentResponseModel = paymentMethodService
            .addPaymentMethod(principal.uid, request)

        return ResponseEntity
            .ok(paymentResponseModel)
    }

    @DeleteMapping
    fun removePaymentMethod(
        @RequestParam(required = true)
        methodId: UUID
    ): ResponseEntity<Unit> {
        paymentMethodService.removePaymentMethod(methodId)
        return ResponseEntity
            .ok()
            .build()
    }
}