package com.foodback.feature.category.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener::class)
internal class CategoryEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,
    var name: String = "",

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isDeleted: Boolean = false,

    @CreatedDate
    val createdAt: Instant = Instant.now()
)