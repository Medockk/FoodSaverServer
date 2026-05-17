package com.foodback.feature.featureRestaurant.impl.controller

import com.foodback.core.coreSecurity.api.dto.Permission
import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.api.dto.UploadRestaurantImageRequest
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.featureRestaurant.api.service.WriteRestaurantService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/v1/restaurant")
internal class RestaurantController(
    private val readRestaurantService: ReadRestaurantService,
    private val writeRestaurantService: WriteRestaurantService
) {

    @GetMapping("/all")
    fun getAllRestaurants(
        @PageableDefault
        @SortDefault.SortDefaults(
            SortDefault(sort = ["rating"], direction = Sort.Direction.ASC)
        )
        pageable: Pageable
    ): ResponseEntity<Page<RestaurantResponse>> {
        val restaurants = readRestaurantService
            .getAllRestaurants(pageable)

        return ResponseEntity.ok(restaurants)
    }

    @GetMapping("id")
    fun getRestaurantById(
        @RequestParam
        restaurantId: UUID
    ): ResponseEntity<RestaurantResponse> {
        val restaurant = readRestaurantService
            .getRestaurantById(restaurantId)

        return if (restaurant == null) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(restaurant)
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADD_RESTAURANT')")
    fun addRestaurant(
        @RequestBody
        request: RestaurantAddRequest
    ): ResponseEntity<RestaurantResponse> {
        val savedRestaurant = writeRestaurantService
            .addRestaurant(request)

        return ResponseEntity.ok(savedRestaurant)
    }

    @PostMapping("/uploadImage")
    @PreAuthorize("hasAuthority('ADD_RESTAURANT')")
    fun uploadRestaurantImage(
        @RequestPart
        image: MultipartFile,
        @RequestParam
        restaurantId: UUID,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<String> {
        val userRestaurantId = principal
            .restaurantId
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        val imageExtension = image
            .originalFilename
            ?.substringAfterLast(".", "png") ?: "png"
        val imageUri = writeRestaurantService
            .uploadRestaurantImage(
                request = UploadRestaurantImageRequest(
                    restaurantId = restaurantId,
                    image = image.bytes,
                    imageExtension = imageExtension
                ),
                userRestaurantId = userRestaurantId
            )

        return ResponseEntity.ok(imageUri)
    }

    @GetMapping("/suggested")
    fun getSuggestedRestaurants(): ResponseEntity<List<RestaurantResponse>> {
        val restaurants = readRestaurantService.getSuggestedRestaurants()
        return if (restaurants.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(restaurants)
    }
}