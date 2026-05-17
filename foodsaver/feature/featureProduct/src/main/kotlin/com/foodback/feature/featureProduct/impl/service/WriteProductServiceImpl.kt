package com.foodback.feature.featureProduct.impl.service

import com.foodback.core.coreMedia.api.service.MediaService
import com.foodback.feature.category.api.service.ReadCategoryService
import com.foodback.feature.featureProduct.api.dto.AddProductRequest
import com.foodback.feature.featureProduct.api.dto.EditProductRequest
import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.UploadProductImageRequest
import com.foodback.feature.featureProduct.api.service.WriteProductService
import com.foodback.feature.featureProduct.impl.exception.ProductNotFoundException
import com.foodback.feature.featureProduct.impl.mapper.ProductMapper
import com.foodback.feature.featureProduct.impl.repository.ProductRepository
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
internal class WriteProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val mediaService: MediaService,
    private val restaurantService: ReadRestaurantService,
    private val readCategoryService: ReadCategoryService
): WriteProductService {

    @Transactional
    override fun addProduct(request: AddProductRequest, userRestaurantId: UUID?): ProductResponse {
        checkPermissions(request.restaurantId, userRestaurantId)

        if (restaurantService.getRestaurantById(request.restaurantId) == null) {
            throw Exception("Missing restaurant")
        }

        val categories = readCategoryService.getAllCategories()
            .map { it.id }
        if (!categories.containsAll(request.categoryIds)) {
            throw Exception("No categories")
        }

        val newEntity = productMapper.toEntity(request)
        val savedProductUri = newEntity.imageUris.map { uri ->
            if (uri.contains("temp")) {
                val newFolder = getProductFolder(request.restaurantId)
                mediaService.moveFromTemp(
                    tempUri = uri,
                    newFolder = newFolder
                )
            } else {
                uri
            }
        }
        newEntity.imageUris = savedProductUri.toMutableList()

        val savedEntity = productRepository.save(newEntity)

        return productMapper.toResponse(savedEntity)
    }

    @Transactional
    override fun editProduct(request: EditProductRequest, userRestaurantId: UUID?): ProductResponse {
        val product = productRepository
            .findById(request.id)
            .orElseThrow { ProductNotFoundException() }

        checkPermissions(product.restaurantId!!, userRestaurantId)

        // updating fields
        request.name?.let { product.name = it }
        request.description?.let { product.description = it }
        request.imageUris?.let { product.imageUris = it.toMutableList() }
        request.price?.let { product.price = it }
        request.discount?.let { product.discount = it }
        request.count?.let { product.count = it }
        request.unit?.let { product.unit = it }
        request.currency?.let { product.currency = it }
        request.isAvailable?.let { product.isAvailable = it }
        request.isDeleted?.let { product.isDeleted = it }
        request.ingredientIds?.let { product.ingredientIds = it.toMutableList() }
        request.categoryIds?.let { product.categoryIds = it.toMutableList() }

        return productMapper.toResponse(product)
    }

    override fun uploadImage(request: UploadProductImageRequest, userRestaurantId: UUID?): String {
        checkPermissions(request.restaurantId, userRestaurantId)

        // checking for new product
        val folder = if (request.productId != null) {
            getProductFolder(request.restaurantId) + "/${request.productId}"
        } else {
            // new product
            getProductFolder(request.restaurantId) + "/temp"
        }

        val imageUri = mediaService.upload(
            bytes = request.image,
            folder = folder,
            extension = request.imageExtension ?: "png"
        )

        return imageUri
    }

    override fun deleteProduct(productId: UUID, userRestaurantId: UUID?) {
        val product = productRepository
            .findById(productId)
            .getOrNull()
            ?: return
        checkPermissions(product.restaurantId!!, userRestaurantId)
        productRepository.deleteById(productId)
    }

    /**
     * if userRestaurantId is null -> role ADMIN or something else
     * if userRestaurantId != request.restaurantId -> пользователь не принадлежит
     * данному ресторану
     */
    private fun checkPermissions(restaurantId: UUID, userRestaurantId: UUID?) {
        if (userRestaurantId != null && userRestaurantId != restaurantId) {
            throw AccessDeniedException("You can only edit product in your restaurant!")
        }
    }

    private fun getProductFolder(restaurantId: UUID): String {
        return "restaurants/$restaurantId/products"
    }
}