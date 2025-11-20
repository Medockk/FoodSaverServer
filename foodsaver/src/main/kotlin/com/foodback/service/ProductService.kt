package com.foodback.service

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.exception.OrganizationException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.mappers.toEntity
import com.foodback.mappers.toResponseModel
import com.foodback.repository.OrganizationRepository
import com.foodback.repository.ProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

/**
 * Special service to do some products logic
 * @param productRepository Special repository to work with database
 */
@Service("productService")
class ProductService(
    private val productRepository: ProductRepository,
    private val organizationRepository: OrganizationRepository
) {

    /**
     * Method to auto-delete expires products
     */
    @Transactional
    @Scheduled(fixedRate = 60_000) // 1 minute
    fun deleteExpiresRecords() {
        val currentTime = Instant.now()
        productRepository.deleteProductAfterExpiresAt(currentTime)
    }

    /**
     * Method to add product to system
     * @param product the product itself
     */
    @Transactional
    fun addProduct(
        product: ProductRequestModel
    ): ProductResponseModel {
        val organizationEntity = organizationRepository.findById(product.organizationId).orElseThrow {
            OrganizationException(
                message = "Organization not found!",
                customCode = RequestError.OrganizationRequest.ORGANIZATION_NOT_FOUND
            )
        }
        return productRepository.save(
            product.toEntity(organizationEntity)
        ).toResponseModel(1)
    }

    /**
     * Method to delete product by [productId] from system
     * @param productId identifier of product, which need to delete
     */
    @Transactional
    fun deleteProduct(
        productId: UUID
    ) {
        productRepository.deleteById(productId)
    }

    /**
     * Method to get all or [productCount] products from database
     * Also get items in [page].
     * @param productCount The count to get some products
     * @param page Table page
     */
    fun getAllProduct(productCount: Int, page: Int = 0): List<ProductResponseModel> {
        val pageable = PageRequest.of(
            page,
            productCount,
            Sort.by(Sort.Direction.DESC, "rating")
        )
        val products = productRepository.findAll(pageable)

        return products.content.mapNotNull { it.toResponseModel() }
    }
}