package com.foodback.feature.featureRestaurant.impl.service

import com.foodback.core.coreMedia.api.service.MediaService
import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.api.dto.UploadRestaurantImageRequest
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.featureRestaurant.api.service.WriteRestaurantService
import com.foodback.feature.featureRestaurant.impl.mapper.RestaurantMapper
import com.foodback.feature.featureRestaurant.impl.repository.RestaurantRepository
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Primary
internal class RestaurantServiceImpl(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantMapper: RestaurantMapper,
    private val mediaService: MediaService
) : ReadRestaurantService, WriteRestaurantService {

    override fun getAllRestaurants(pageable: Pageable): Page<RestaurantResponse> {
        return restaurantRepository
            .findAll(pageable)
            .map {
                restaurantMapper.toResponse(it)
            }
    }

    override fun getRestaurantById(id: UUID): RestaurantResponse? {
        val restaurant = restaurantRepository.findById(id)
            .getOrNull() ?: return null

        return restaurantMapper.toResponse(restaurant)
    }

    override fun getSuggestedRestaurants(): List<RestaurantResponse> {
        val suggestedRestaurants = restaurantRepository.findAllByIsSuggestedTrue()
        return suggestedRestaurants.map {
            restaurantMapper.toResponse(it)
        }
    }

    @Transactional
    override fun addRestaurant(request: RestaurantAddRequest): RestaurantResponse {

        val newEntity = restaurantMapper.toEntity(request)
        val savedEntity = restaurantRepository.save(newEntity)

        return restaurantMapper.toResponse(savedEntity)
    }

    override fun uploadRestaurantImage(
        request: UploadRestaurantImageRequest,
        userRestaurantId: UUID?
    ): String {
        if (userRestaurantId != null && userRestaurantId != request.restaurantId) {
            throw AccessDeniedException("Doesn't has authority!")
        }

        val folder = "restaurants/${request.restaurantId}/images"
        val imageUri = mediaService.upload(
            bytes = request.image,
            folder = folder,
            extension = request.imageExtension ?: "png",
        )
        return imageUri
    }
}