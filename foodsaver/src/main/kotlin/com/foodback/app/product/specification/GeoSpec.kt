package com.foodback.app.product.specification

import com.foodback.app.enterprises.entity.EnterprisesEntity
import com.foodback.app.product.entity.ProductEntity
import com.foodback.app.product.service.GeoUtils
import org.springframework.data.jpa.domain.Specification

object GeoSpec {

    fun isNearUser(userLatitude: Double?, userLongitude: Double?, radiusKm: Double): Specification<ProductEntity> {
        return Specification { root, _, builder ->
            if (userLatitude == null) return@Specification null
            if (userLongitude == null) return@Specification null

            val box = GeoUtils.calculateGeoBox(userLatitude, userLongitude, radiusKm)
            val enterpriseTable = root.join<ProductEntity, EnterprisesEntity>("enterprise")
            val latitude = builder.between(
                enterpriseTable?.get("latitude"),
                box.minLatitude,
                box.maxLatitude
            )
            val longitude = builder.between(
                enterpriseTable?.get("longitude"),
                box.minLongitude,
                box.maxLongitude
            )

            builder.and(latitude, longitude)
        }
    }
}