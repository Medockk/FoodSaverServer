package com.foodback.app.product.entity

import com.foodback.app.enterprises.entity.EnterprisesEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.*

/**
 * Database entity of organizations
 * @param id Unique identifier of current organization
 * @param organizationName Name of organization
 * @param owner The owner of current organization
 * @param createdAt Date, when this organization will add to database
 */
@Entity
@Table(name = "organizations")
data class OrganizationEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(name = "name", nullable = false, unique = true)
    var organizationName: String,

    @Column(name = "owner", nullable = false)
    var owner: String = "",

    @CreatedDate
    var createdAt: Instant? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "organization"
    )
    var enterprises: MutableList<EnterprisesEntity> = mutableListOf()
)
