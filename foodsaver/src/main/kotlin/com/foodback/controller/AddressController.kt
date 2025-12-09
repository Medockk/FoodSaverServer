package com.foodback.controller

import com.foodback.dto.request.address.AddAddressRequestModel
import com.foodback.dto.response.address.AddressResponseModel
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.service.AddressService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/address")
class AddressController(
    private val addressService: AddressService
) {

    @PostMapping("add")
    fun addAddress(
        @RequestBody
        addAddressRequestModel: AddAddressRequestModel,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<AddressResponseModel> {
        val address = addressService.addAddress(addAddressRequestModel, principal.uid)
        return ResponseEntity
            .ok(address)
    }

    @GetMapping("all")
    fun getAllAddresses(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<List<AddressResponseModel>> {
        val addresses = addressService.getAllAddresses(principal.uid)
        return ResponseEntity
            .ok(addresses)
    }

    @GetMapping("current")
    fun getCurrentAddress(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<AddressResponseModel> {
        val address: AddressResponseModel? = addressService.getCurrentAddress(principal.uid)
        return if (address == null) {
            println("Address null. No content!")
            ResponseEntity
                .noContent()
                .build()
        } else {
            ResponseEntity.ok(address)
        }
    }

    @PutMapping("setCurrent")
    fun setCurrentAddress(
        @RequestParam
        addressId: UUID,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<AddressResponseModel> {
        val newAddress = addressService.setCurrentAddress(addressId, principal.uid)
        return ResponseEntity
            .ok(newAddress)
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