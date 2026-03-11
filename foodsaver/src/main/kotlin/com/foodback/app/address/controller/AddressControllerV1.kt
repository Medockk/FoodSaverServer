package com.foodback.app.address.controller

import com.foodback.app.address.dto.request.AddAddressRequestModelV1
import com.foodback.app.address.dto.response.AddressResponseModelV1
import com.foodback.app.address.mapper.AddressMapperV1
import com.foodback.app.address.service.AddressService
import com.foodback.security.auth.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/address")
class AddressControllerV1(
    private val addressService: AddressService,
    private val addressMapperV1: AddressMapperV1
) {

    @PostMapping("add")
    fun addAddress(
        @RequestBody
        addAddressRequestModelV1: AddAddressRequestModelV1,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<AddressResponseModelV1> {
        val address = addressService.addAddress(addAddressRequestModelV1, principal.uid)
        return ResponseEntity
            .ok(addressMapperV1.mapToResponse(address))
    }

    @GetMapping("all")
    fun getAllAddresses(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<List<AddressResponseModelV1>> {
        val addresses = addressService.getAllAddresses(principal.uid)
        return if (addresses.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity
            .ok(addresses.map { addressMapperV1.mapToResponse(it) })
    }

    @GetMapping("current")
    fun getCurrentAddress(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<AddressResponseModelV1> {
        val address = addressService.getCurrentAddress(principal.uid)
        return if (address == null) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(addressMapperV1.mapToResponse(address))
        }
    }

    @PutMapping("setCurrent")
    fun setCurrentAddress(
        @RequestParam
        addressId: UUID,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<AddressResponseModelV1> {
        val newAddress = addressService.setCurrentAddress(addressId, principal.uid)
        return ResponseEntity
            .ok(addressMapperV1.mapToResponse(newAddress))
    }

    @DeleteMapping("delete")
    fun deleteAddress(
        @RequestParam
        addressId: UUID,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .ok(addressService.deleteAddress(addressId, principal.uid))
    }
}