package com.foodback.demo.service

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.request.mappers.toEntity
import com.foodback.demo.dto.request.mappers.toResponseModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.repository.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

/**
 * Special service to
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional
    @Scheduled(fixedRate = 60_000) // 1 minute
    fun deleteExpiresRecords() {

        val currentTime = Instant.now()
        val result = productRepository.deleteProductAfterExpiresAt(currentTime)
        println(result)
    }

    fun addProduct(
        product: ProductRequestModel
    ): ProductResponseModel {
        return productRepository.save(
            product.toEntity()
        ).toResponseModel(1)
    }

    @Transactional
    fun deleteProduct(
        id: UUID
    ) {

    }
}