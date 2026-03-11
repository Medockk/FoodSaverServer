package com.foodback.app.product.service

import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.dto.request.ProductRequestModel
import com.foodback.app.product.dto.response.CategoriesResponseModel
import com.foodback.app.product.entity.ProductEntity
import com.foodback.app.product.mapper.toResponse
import com.foodback.app.product.repository.OrganizationRepository
import com.foodback.app.product.repository.ProductCategoriesRepository
import com.foodback.app.product.repository.ProductRepository
import com.foodback.exception.category.ProductCategoriesException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.exception.organization.OrganizationException
import com.foodback.exception.product.ProductNotFoundException
import com.foodback.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
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
    private val productCategoriesRepository: ProductCategoriesRepository,

    private val mediaService: MediaService,
    @Value($$"${app.media.path.products}")
    private val uploadPath: String
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
        product: ProductRequestModel,
        organizationId: UUID,
        file: MultipartFile?
    ): ProductEntity {
        val organizationEntity = organizationRepository.findById(organizationId).orElseThrow {
            OrganizationException(
                message = "Organization not found!",
                customCode = RequestError.OrganizationRequest.ORGANIZATION_NOT_FOUND
            )
        }
        val categoryEntity = if (product.categoryIds.isEmpty()) mutableListOf()
        else productCategoriesRepository.findAllById(product.categoryIds).ifEmpty {
            throw ProductCategoriesException(
                "CategoryEntity with ids ${product.categoryIds} not found!",
                RequestError.ProductCategoriesRequest.CATEGORY_NOT_FOUND
            )
        }

        val relativeUrl = file?.let {
            if (file.isEmpty) return@let null
            mediaService.uploadImage(
                file = file,
                baseUploadPath = uploadPath,
                fileUrlType = MediaService.FileUrlType.PRODUCTS
            )
        }

        val entity = ProductEntity(
            title = product.title,
            description = product.description,
            cost = product.cost,
            costUnit = product.costUnit,
            organization = organizationEntity,
            rating = 0f,
            count = product.count,
            categories = categoryEntity,
            photoUrl = relativeUrl,
            unit = product.unit,
            unitName = product.unitName,
            addedAt = Instant.now(),
            expiresAt = product.expiresAt
        )
        return productRepository.save(entity)
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
    fun getAllProduct(pageable: Pageable): List<ProductEntity> {
        val products = productRepository.findAll(pageable)

        return products.content
    }

    /**
     * Method to find a [ProductResponseModel] with [productId]
     * @param productId Identifier of product
     * @return A [ProductResponseModel]
     * @throws ProductNotFoundException if product not found
     */
    fun getProductById(productId: UUID): ProductEntity {
        val product = productRepository.findById(productId).orElseThrow {
            ProductNotFoundException("Product with id $productId not found")
        }
        return product
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
    ): List<ProductEntity> {
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

        return products.content
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
    ): List<ProductEntity> {
        val products = if (categoryId == null) {
            val products = productRepository.findAllByOrganization_Id(
                organizationId = organizationId,
                pageable = pageable
            )
            products.content
        } else {
            val products = productRepository.findAllByOrganization_IdAndCategories_Id(
                organizationId = organizationId,
                categoryId = categoryId,
                pageable = pageable
            )
            products.content
        }

        return products
    }
}