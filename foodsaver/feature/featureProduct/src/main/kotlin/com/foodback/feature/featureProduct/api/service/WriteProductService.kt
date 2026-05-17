package com.foodback.feature.featureProduct.api.service

import com.foodback.feature.featureProduct.api.dto.AddProductRequest
import com.foodback.feature.featureProduct.api.dto.EditProductRequest
import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.UploadProductImageRequest
import java.util.UUID

interface WriteProductService {

    fun addProduct(request: AddProductRequest, userRestaurantId: UUID?): ProductResponse
    fun editProduct(request: EditProductRequest, userRestaurantId: UUID?): ProductResponse
    fun uploadImage(request: UploadProductImageRequest, userRestaurantId: UUID?): String

    fun deleteProduct(productId: UUID, userRestaurantId: UUID?)
}