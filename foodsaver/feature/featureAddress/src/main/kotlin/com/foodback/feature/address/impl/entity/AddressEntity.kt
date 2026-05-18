package com.foodback.feature.address.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "addresses")
@EntityListeners(AuditingEntityListener::class)
internal class AddressEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var latitude: Double = 0.0,
    @Column(nullable = false)
    var longitude: Double = 0.0,

    @Column(columnDefinition = "geography(Point, 4326)")
    var coords: Point? = null,

    @Column(nullable = false)
    var city: String = "",
    @Column(nullable = false)
    var street: String = "",
    @Column(nullable = false)
    var house: String = "",

    var apartment: String? = null,
    var floor: Int? = null,
    var entrance: String? = null,

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    var name: String = "",

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isDeleted: Boolean = false,

    @CreatedDate
    var createdAt: Instant = Instant.now()
) {
    fun updateCoords(lat: Double, lon: Double, geometryFactory: GeometryFactory) {
        this.latitude = lat
        this.longitude = lon
        this.coords = geometryFactory.createPoint(Coordinate(lon, lat))
    }
}