package com.foodback.controller

import com.foodback.dto.request.offer.OfferRequestModel
import com.foodback.dto.response.offer.OfferResponseModel
import com.foodback.service.OffersService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/offers")
class OffersController(
    private val offersService: OffersService
) {

    @GetMapping
    fun getOffers(): ResponseEntity<List<OfferResponseModel>> {
        return ResponseEntity
            .ok(offersService.getOffers())
    }

    @PreAuthorize(value = "hasRole('ADMIN') OR hasRole('MANAGER')")
    fun postOffer(@RequestBody(required = true) requestModel: OfferRequestModel): ResponseEntity<Void> {
        return try {
            offersService.postOffer(requestModel)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build<Void>()
            throw e
        }
    }
}