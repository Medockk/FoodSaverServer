package com.foodback.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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
)
