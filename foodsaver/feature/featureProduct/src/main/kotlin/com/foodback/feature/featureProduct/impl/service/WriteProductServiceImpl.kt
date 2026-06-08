package com.foodback.feature.featureProduct.impl.service

import com.foodback.core.coreEvent.api.event.ai.ValidateProductFreshnessEvent
import com.foodback.core.coreMedia.api.service.MediaService
import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.feature.category.api.service.ReadCategoryService
import com.foodback.feature.featureIngredients.api.service.ReadIngredientsService
import com.foodback.feature.featureProduct.api.dto.*
import com.foodback.feature.featureProduct.api.service.WriteProductService
import com.foodback.feature.featureProduct.impl.exception.ProductNotFoundException
import com.foodback.feature.featureProduct.impl.exception.ProductNotFreshException
import com.foodback.feature.featureProduct.impl.mapper.ProductMapper
import com.foodback.feature.featureProduct.impl.repository.ProductRepository
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import org.springframework.context.ApplicationEventPublisher
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
    private val mediaUriMapperService: MediaUriMapperService,
    private val restaurantService: ReadRestaurantService,
    private val readCategoryService: ReadCategoryService,
    private val ingredientsService: ReadIngredientsService,

    private val applicationEventPublisher: ApplicationEventPublisher
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

        val ingredients = ingredientsService.getAllIngredients()
            .map { it.id }
        if (!ingredients.containsAll(request.ingredientIds)) {
            throw Exception("Some ingredients missing")
        }

        val newEntity = productMapper.toEntity(request)
        val savedProductUri = newEntity.imageUris.map { uri ->
            if (uri.contains("temp")) {
                val newFolder = getProductFolder(request.restaurantId)
                println("WriteProductServiceImpl addProduct грязный URI: $uri")
                val relativeTempUri = mediaUriMapperService.toRelativeUri(uri)
                println("WriteProductServiceImpl addProduct чистый URI: $relativeTempUri")
                mediaService.moveFromTemp(
                    tempUri = relativeTempUri,
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

    override fun uploadImage(request: UploadProductImageRequest, userRestaurantId: UUID?): UploadImageResponse {
        checkPermissions(request.restaurantId, userRestaurantId)

//        val event = ValidateProductFreshnessEvent(
//            image = request.image,
//            imageName = "food_image.${request.imageExtension ?: "png"}"
//        )
//        applicationEventPublisher.publishEvent(event)
//        println("AI Result ${event.result}")
//
//        val aiResult = event.result
//            ?: throw ProductNotFreshException()
//        if (!aiResult.isFresh) {
//            throw ProductNotFreshException()
//        }

        // checking for new product
        val folder = if (request.productId != null) {
            getProductFolder(request.restaurantId) + "/${request.productId}"
        } else {
            // new product
            getProductFolder(request.restaurantId) + "/temp"
        }

        val relativeUri = mediaService.upload(
            bytes = request.image,
            folder = folder,
            extension = request.imageExtension ?: "png"
        )
        val absoluteUri = mediaUriMapperService.toAbsoluteUri(relativeUri)

        return UploadImageResponse(relativeUri, absoluteUri)
    }

    override fun deleteProduct(productId: UUID, userRestaurantId: UUID?) {
        val product = productRepository
            .findById(productId)
            .getOrNull()
            ?: return
        checkPermissions(product.restaurantId!!, userRestaurantId)
        productRepository.deleteById(productId)
    }

    @Transactional
    override fun decreaseProductCount(productId: UUID, quantity: Long) {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException() }

        if (product.count < quantity) {
            throw IllegalStateException("Недостаточно товара '${product.name}' на складе. Доступно: ${product.count}, запрошено: $quantity")
        }

        product.count -= quantity
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