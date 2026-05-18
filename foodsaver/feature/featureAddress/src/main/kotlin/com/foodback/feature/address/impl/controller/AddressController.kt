package com.foodback.feature.address.impl.controller

import com.foodback.feature.address.api.dto.AddAddressRequest
import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.address.api.service.AddressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/address")
internal class AddressController(
    private val addressService: AddressService
) {

    @GetMapping("/id")
    fun getAddress(
        @RequestParam
        id: UUID
    ): ResponseEntity<AddressResponse> {
        val response = addressService.getAddressById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/ids")
    fun getAddressesByIds(
        @RequestParam
        ids: List<UUID>
    ): ResponseEntity<List<AddressResponse>> {
        val addresses = addressService.getAddressesByIds(ids)
        return if (addresses.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(addresses)
    }

    @PostMapping("/add")
    fun addAddress(
        @RequestBody
        request: AddAddressRequest
    ): ResponseEntity<AddressResponse> {
        val response = addressService.addAddress(request)
        return ResponseEntity.ok(response)
    }

    // Эндпоинт на удаление будет в ProfileController, т.к. иначе любой человек может удалить любой адрес
}