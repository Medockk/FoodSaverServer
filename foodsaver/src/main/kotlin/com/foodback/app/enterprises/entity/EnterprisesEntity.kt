package com.foodback.app.enterprises.entity

import com.foodback.app.product.entity.OrganizationEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.locationtech.jts.geom.Point
import java.util.UUID

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
)
