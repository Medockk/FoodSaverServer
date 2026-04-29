package com.foodback.app.product.service

import com.foodback.app.cart.service.CartService
import com.foodback.app.product.entity.ProductEntity
import com.foodback.app.product.repository.ProductRepository
import com.foodback.app.product.specification.GeoSpec
import com.foodback.app.product.specification.ProductSpec
import com.foodback.app.user.repository.UserLocationRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class PersonalizationService(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartService,
    private val userLocationRepository: UserLocationRepository
) {

    @Transactional(readOnly = true)
    fun personalizeProducts(input: PersonalizationInputData, pageable: Pageable): List<ProductEntity> {
        val cartItems = cartItemRepository.getUserCart(input.uid)
        val categories = cartItems
            .flatMap { it.product.categories }
            .mapNotNull { it.id }
            .toSet()

        val location = userLocationRepository
            .findByUserUid(uid = input.uid)
            .getOrNull()
        val context = PersonalizationContext(
            userId = input.uid,
            favoriteCategoryIds = categories,
            userLocation = PersonalizationInputData.UserLocation(
                latitude = location?.latitude,
                longitude = location?.longitude
            )
        )

        val favoriteSpec = ProductSpec.fromFavoriteCategories(context.favoriteCategoryIds)
        val locationSpec = GeoSpec.isNearUser(
            userLatitude = context.userLocation?.latitude,
            userLongitude = context.userLocation?.longitude,
            radiusKm = input.searchRadiusKm
        )
        val spec = when (input.searchType) {
            PersonalizationInputData.SearchType.NEARBY -> {
                Specification.where(locationSpec)
            }
            PersonalizationInputData.SearchType.RECOMMENDED -> {
                Specification.where(locationSpec).and(favoriteSpec)
            }
        }
        val products = productRepository.findAll(spec, pageable)
        val content = products.content
        println("Content is ${content.size}")
        return content
    }

    private data class PersonalizationContext(
        val userId: UUID,
        val favoriteCategoryIds: Set<UUID>,
        val userLocation: PersonalizationInputData.UserLocation?,
    )
}