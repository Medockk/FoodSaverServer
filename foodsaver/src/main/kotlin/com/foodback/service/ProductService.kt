package com.foodback.service

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.CategoriesResponseModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.exception.OrganizationException
import com.foodback.exception.category.ProductCategoriesException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.exception.product.ProductNotFoundException
import com.foodback.mappers.toEntity
import com.foodback.mappers.toResponse
import com.foodback.mappers.toResponseModel
import com.foodback.repository.OrganizationRepository
import com.foodback.repository.ProductCategoriesRepository
import com.foodback.repository.ProductRepository
import org.springframework.data.domain.Pageable
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
        val categoryEntity = productCategoriesRepository.findAllById(product.categoryIds).ifEmpty {
            throw ProductCategoriesException(
                "CategoryEntity with ids ${product.categoryIds} not found!",
                RequestError.ProductCategoriesRequest.CATEGORY_NOT_FOUND
            )
        }

        return productRepository.save(
            product.toEntity(organizationEntity).apply {
                this.categories = categoryEntity
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
     * Method to get some [ProductResponseModel] from database
     * @param pageable This parameter set LIMIT and OFFSET in SQL-query
     * @return A [List] of [ProductResponseModel]
     */
    fun getAllProduct(pageable: Pageable): List<ProductResponseModel> {
        val products = productRepository.findAll(pageable)

        return products.content.mapNotNull { it.toResponseModel() }
    }

    /**
     * Method to find a [ProductResponseModel] with [productId]
     * @param productId Identifier of product
     * @return A [ProductResponseModel]
     * @throws ProductNotFoundException if product not found
     */
    fun getProductById(productId: UUID): ProductResponseModel {
        val product = productRepository.findById(productId).orElseThrow {
            ProductNotFoundException("Product with id $productId not found")
        }
        return product.toResponseModel()
    }

    /**
     * Method to get all categories
     * @return A [List] of [CategoriesResponseModel]
     */
    fun getAllCategories(): List<CategoriesResponseModel> {
        return productCategoriesRepository.findAll().map {
            it.toResponse()
        }
    }

    fun searchProducts(
        productName: String,
        categoryIds: List<UUID>,
        pageable: Pageable
    ): List<ProductResponseModel> {
        val products = if (categoryIds.isEmpty()) {
            productRepository.findAllByTitleContainingIgnoreCase(productName, pageable)
        } else if (productName.isBlank() && categoryIds.isNotEmpty()) {
            productRepository.findAllByCategories_IdIn(categoryIds, pageable)
        } else {
            productRepository.findAllByTitleContainingIgnoreCaseAndCategories_IdIn(
                title = productName,
                categoryIds = categoryIds,
                pageable = pageable
            )
        }

        return products.content.map { it.toResponseModel() }
    }

    /**
     * Method to get all [ProductResponseModel] belongs to some organization. If [categoryId] isn't null
     * return all [ProductResponseModel] of special organization with special category
     * @param organizationId The identifier of organization
     * @param categoryId The identifier of product category
     * @param pageable This parameter set LIMIT and OFFSET in SQL-query
     * @return A [List] of [ProductResponseModel] belongs special [ProductResponseModel.organization] with special category,
     * if [categoryId] isn't null
     */
    fun getProductsByOrganization(
        organizationId: UUID,
        categoryId: UUID? = null,
        pageable: Pageable
    ): List<ProductResponseModel> {
        val products = if (categoryId == null) {
            val products = productRepository.findAllByOrganization_Id(
                organizationId = organizationId,
                pageable = pageable
            )
            products.content.map { it.toResponseModel() }
        } else {
            val products = productRepository.findAllByOrganization_IdAndCategories_Id(
                organizationId = organizationId,
                categoryId = categoryId,
                pageable = pageable
            )
            products.content.map { it.toResponseModel() }
        }

        return products
    }
}