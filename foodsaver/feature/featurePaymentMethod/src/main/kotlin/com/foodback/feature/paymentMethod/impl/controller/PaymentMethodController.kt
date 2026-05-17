package com.foodback.feature.paymentMethod.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.paymentMethod.api.dto.AddPaymentMethodRequest
import com.foodback.feature.paymentMethod.api.dto.PaymentMethodResponse
import com.foodback.feature.paymentMethod.api.dto.PaymentMethodTypeResponse
import com.foodback.feature.paymentMethod.api.service.PaymentMethodService
import com.foodback.feature.paymentMethod.api.service.PaymentMethodTypeService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/paymentMethod")
internal class PaymentMethodController(
    private val paymentTypesService: PaymentMethodTypeService,
    private val paymentMethodService: PaymentMethodService
) {

    @GetMapping("/types")
    fun getPaymentMethodTypes(): ResponseEntity<List<PaymentMethodTypeResponse>> {
        val types = paymentTypesService.getTypes()
        return if (types.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(types)
    }

    // ok
    @GetMapping("/my")
    fun getUserCurrentPaymentMethods(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<List<PaymentMethodResponse>> {
        val methods = paymentMethodService
            .getUserPaymentMethods(principal.uid)

        return if (methods.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(methods)
    }

    @PostMapping("/add")
    fun addPaymentMethod(
        @RequestBody
        request: AddPaymentMethodRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<PaymentMethodResponse> {
        val response = paymentMethodService.addPaymentMethod(request, principal.uid)
        return ResponseEntity.ok(response)

    }
}