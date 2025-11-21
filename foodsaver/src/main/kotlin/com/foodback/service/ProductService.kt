package com.foodback.service

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.exception.OrganizationException
import com.foodback.exception.category.ProductCategoriesException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.mappers.toEntity
import com.foodback.mappers.toResponseModel
import com.foodback.repository.OrganizationRepository
import com.foodback.repository.ProductCategoriesRepository
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
    private val organizationRepository: OrganizationRepository,
    private val productCategoriesRepository: ProductCategoriesRepository
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
        val categories = productCategoriesRepository.findAllById(product.categoryIds).ifEmpty {
            throw ProductCategoriesException("Categories with ids ${product.categoryIds} not found!", RequestError.ProductCategoriesRequest.CATEGORY_NOT_FOUND)
        }

        return productRepository.save(
            product.toEntity(organizationEntity).apply {
                this.categories = categories
            }
        ).toResponseModel()
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

    /**
     * Method to find products by they [name]
     * @param name Product name to find products
     * @return A [List] of [ProductResponseModel], containing some [name]
     */
    fun findProduct(name: String): List<ProductResponseModel> {
        val productsEntity = productRepository.findAllByTitleContainingIgnoreCase(name)
        val products = productsEntity.map { it.toResponseModel() }
        return products
    }

    /**
     * Method to find all products with special [categoryId]
     * @param categoryId Identifier of category, to find products
     * @return A [List] of [ProductResponseModel] with special category
     */
    fun findProductsByCategory(categoryId: UUID): List<ProductResponseModel> {
        val products = productRepository.findAllByCategories_Id(categoryId)
        return products.map { it.toResponseModel() }
    }
}