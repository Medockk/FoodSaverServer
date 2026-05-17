package com.foodback.feature.base.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.base.api.dto.OrderResponse
import com.foodback.feature.base.api.service.ReadOrderService
import com.foodback.feature.base.api.service.WriteOrderService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/order")
internal class OrderController(
    private val readOrderService: ReadOrderService,
//    private val writeOrderService: WriteOrderService
) {

    @GetMapping("/my")
    fun getOrders(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<List<OrderResponse>> {
        val orders = readOrderService
            .getOrders(principal.uid)

        return if (orders.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(orders)
    }
}