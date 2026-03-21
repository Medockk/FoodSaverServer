package com.foodback.app.product.service

import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.dto.request.ProductRequestModel
import com.foodback.app.product.entity.CategoryEntity
import com.foodback.app.product.entity.ProductEntity
import com.foodback.app.product.repository.ProductRepository
import com.foodback.exception.product.ProductNotFoundException
import com.foodback.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
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
    private val mediaService: MediaService,
    @Value($$"${app.media.path.products}")
    private val uploadPath: String
) {

    /**
     * Method to add product to system
     * @param product the product itself
     */
    @Transactional
    fun addProduct(
        product: ProductRequestModel,
        categories: List<CategoryEntity>,
        file: MultipartFile?
    ): ProductEntity {
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
            rating = 0f,
            count = product.count,
            categories = categories.toMutableList(),
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
}