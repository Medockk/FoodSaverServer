package com.foodback.app.offers.service

import com.foodback.app.offers.entity.OffersEntity
import com.foodback.app.offers.mapper.OfferMappers
import com.foodback.app.offers.repository.OffersRepository
import com.foodback.app.offers.dto.request.OfferRequestModel
import com.foodback.app.offers.dto.response.OfferResponseModel
import com.foodback.exception.offer.OfferException
import com.foodback.app.product.repository.ProductRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OffersService(
    private val offersRepository: OffersRepository,
    private val productRepository: ProductRepository,
    private val offerMappers: OfferMappers
) {

    fun getOffers(): List<OfferResponseModel> {
        val offers = with(offerMappers) {
            offersRepository
                .findAll()
                .toResponseModel()
        }

        return offers
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    fun postOffer(offer: OfferRequestModel) {
        val product = productRepository
            .findById(offer.productId)
            .orElseThrow {
                OfferException(message = "Product not found")
            }
        val entity = OffersEntity(
            title = offer.title,
            description = offer.description,
            productEntity = product
        )

        offersRepository.save(entity)
    }
}