package com.foodback.feature.featureProduct.impl.service

import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.SearchProductsRequest
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.featureProduct.impl.exception.ProductNotFoundException
import com.foodback.feature.featureProduct.impl.mapper.ProductMapper
import com.foodback.feature.featureProduct.impl.repository.ProductRepository
import com.foodback.feature.featureProduct.impl.util.SearchProductSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class ReadProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper
) : ReadProductService {

    override fun getProductById(productId: UUID): ProductResponse {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException() }
        return productMapper.toResponse(product)
    }

    override fun getProductsByIds(ids: List<UUID>): List<ProductResponse> {
        val products = productRepository
            .findAllById(ids)
        return products.map {
            productMapper.toResponse(it)
        }
    }

    override fun getAllProducts(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAllByIsDeletedFalse(pageable).map {
            productMapper.toResponse(it)
        }
    }

    override fun getAllProductByRestaurantId(
        restaurantId: UUID,
        pageable: Pageable
    ): Page<ProductResponse> {
        return productRepository.findAllByRestaurantIdAndIsDeletedFalse(
            restaurantId = restaurantId,
            pageable = pageable
        ).map {
            productMapper.toResponse(it)
        }
    }

    override fun getAllProductsByCategoryIds(
        categoryIds: List<UUID>,
        pageable: Pageable
    ): Page<ProductResponse> {
        return productRepository.findAllByCategoryIdsIn(categoryIds, pageable).map {
            productMapper.toResponse(it)
        }
    }

    override fun searchProducts(
        request: SearchProductsRequest,
        pageable: Pageable
    ): Page<ProductResponse> {
        val spec = Specification.where(SearchProductSpecification.isNotDeleted())
            .and(
                SearchProductSpecification.globalSearch(
                    query = request.query,
                    discoveredIds = request.discoveredIds,
                )
            )


        val products = productRepository
            .findAll(spec, pageable)

        return products.map {
            productMapper.toResponse(it)
        }
    }

    override fun getSuggestedProducts(): List<ProductResponse> {
        val suggestedProducts = productRepository.findAllByIsSuggestedTrue()
        return suggestedProducts.map {
            productMapper.toResponse(it)
        }
    }
}