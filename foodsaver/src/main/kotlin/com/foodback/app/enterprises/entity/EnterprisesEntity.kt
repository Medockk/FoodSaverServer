package com.foodback.app.enterprises.entity

import com.foodback.app.product.entity.OrganizationEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.locationtech.jts.geom.Point
import java.util.*

@Entity
@Table(name = "enterprises")
data class EnterprisesEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false)
    var latitude: Double = 0.0,
    @Column(nullable = false)
    var longitude: Double = 0.0,

    @Column(nullable = false)
    var addressName: String = "",

    @Column(columnDefinition = "geography(Point, 4326)")
    var coords: Point? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: OrganizationEntity? = null,

    @Column(nullable = true)
    var imageUrls: MutableList<String>? = mutableListOf(),
) {
    override fun toString(): String {
        return """
            ${this::class.java.simpleName}(
                id = $id,
                latitude = $latitude,
                longitude = $longitude,
                addressName = $addressName,
                coords = $coords,
                organization = ${organization?.id},
                imageUrls = $imageUrls
            )
        """.trimIndent()
    }
}
