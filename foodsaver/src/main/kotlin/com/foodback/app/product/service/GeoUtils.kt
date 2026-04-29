package com.foodback.app.product.service

import kotlin.math.cos

object GeoUtils {

    data class GeoBox(
        val minLatitude: Double,
        val maxLatitude: Double,
        val minLongitude: Double,
        val maxLongitude: Double,
    )

    private const val KM_LAT: Double = 111.13 // 1 градус долготы (или широты)

    fun calculateGeoBox(userLatitude: Double, userLongitude: Double, radiusKm: Double): GeoBox {
        val deltaLatitude = radiusKm / KM_LAT
        val deltaLongitude = radiusKm / (KM_LAT * cos(Math.toRadians(userLatitude)))
        
        val geoBox = GeoBox(
            minLatitude = userLatitude - deltaLatitude,
            maxLatitude = userLatitude + deltaLatitude,
            minLongitude = userLongitude - deltaLongitude,
            maxLongitude = userLongitude + deltaLongitude
        )
        return geoBox
    }
}