package com.foodback.demo.service

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.mappers.toEntity
import com.foodback.demo.mappers.toResponseModel
import com.foodback.demo.repository.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

/**
 * Special service to do some products logic
 * @param productRepository Special repository to work with database
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    /**
     * Method to auto-delete expires products
     */
    @Transactional
    @Scheduled(fixedRate = 60_000) // 1 minute
    fun deleteExpiresRecords() {

        val currentTime = Instant.now()
        val result = productRepository.deleteProductAfterExpiresAt(currentTime)
        println(result)
    }

    /**
     * Method to add product to system
     * @param product the product itself
     */
    @Transactional
    fun addProduct(
        product: ProductRequestModel
    ): ProductResponseModel {
        return productRepository.save(
            product.toEntity()
        ).toResponseModel(1)
    }

    /**
     * Method to delete product by [id] from system
     * @param id product id, which need to delete
     */
    @Transactional
    fun deleteProduct(
        id: UUID
    ) {

    }
}