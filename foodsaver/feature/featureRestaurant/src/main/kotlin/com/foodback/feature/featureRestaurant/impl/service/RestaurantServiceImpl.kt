package com.foodback.feature.featureRestaurant.impl.service

import com.foodback.core.coreMedia.api.service.MediaService
import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.api.dto.UpdateRestaurantRequest
import com.foodback.feature.featureRestaurant.api.dto.UploadRestaurantImageRequest
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.featureRestaurant.api.service.WriteRestaurantService
import com.foodback.feature.featureRestaurant.impl.entity.AddressEntity
import com.foodback.feature.featureRestaurant.impl.mapper.RestaurantMapper
import com.foodback.feature.featureRestaurant.impl.repository.RestaurantRepository
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
@Primary
internal class RestaurantServiceImpl(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantMapper: RestaurantMapper,
    private val mediaService: MediaService,
    private val mediaUriMapperService: MediaUriMapperService
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

    override fun getRestaurantsByIds(ids: List<UUID>): List<RestaurantResponse> {
        val restaurants = restaurantRepository.findAllById(ids)
        return restaurants.map { restaurantMapper.toResponse(it) }
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

        val uris = savedEntity.photoUris.map { uri ->
            if (uri.contains("/temp")) {
                val newFolder = "restaurants/${savedEntity.id!!}/images"
                val relativeTempUri = mediaUriMapperService.toRelativeUri(uri)
                mediaService.moveFromTemp(relativeTempUri, newFolder)
            } else {
                uri
            }
        }
        savedEntity.photoUris = uris.toMutableList()


        return restaurantMapper.toResponse(savedEntity)
    }

    override fun uploadRestaurantImage(
        request: UploadRestaurantImageRequest,
        userRestaurantId: UUID?
    ): String {
        println("Upload restaurant image.\nUserRestaurantId $userRestaurantId\nRequestRestaurantId ${request.restaurantId}")
        // if userRestaurantId == null -> Admin / some unauthorized user
        if (userRestaurantId != null && userRestaurantId != request.restaurantId) {
            throw AccessDeniedException("Doesn't has authority!")
        }

        val folder = if (request.restaurantId != null) {
            "restaurants/${request.restaurantId}/images"
        } else {
            "restaurants/temp" + "/${UUID.randomUUID()}"
        }
        val imageUri = mediaService.upload(
            bytes = request.image,
            folder = folder,
            extension = request.imageExtension ?: "png",
        )

        return mediaUriMapperService.toAbsoluteUri(imageUri)
    }

    @Transactional
    override fun updateRestaurant(request: UpdateRestaurantRequest): RestaurantResponse {
        val restaurant = restaurantRepository.findById(request.restaurantId)
            .orElseThrow { Exception() }

        request.name?.let { restaurant.name = it }
        request.description?.let { restaurant.description = it }
        request.photoUris?.let {
            restaurant.photoUris = it.toMutableList()
            println("Update restaurant photo uris $it")
        }
        request.rating?.let { restaurant.rating = it }
        request.averageDeliveryTime?.let { restaurant.averageDeliveryTime = it }
        request.deliveryCost?.let { restaurant.deliveryCost = it }
        request.address?.let { restaurant.address = AddressEntity(
            addressName = it.addressName,
            latitude = it.latitude,
            longitude = it.longitude
        ) }

        return restaurantMapper.toResponse(restaurant)
    }

    override fun deleteRestaurant(id: UUID) {
        try {
            restaurantRepository.deleteById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}