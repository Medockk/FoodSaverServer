package com.foodback.base.module.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "companies")
internal class CompanyEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID = UUID.randomUUID(),

    var companyName: String = "",
    var companyDescription: String? = null,

    var ownerFullName: String = "",
    @Column(name = "owner_user_id")
    var ownerId: UUID? = null,
    var logoUri: String? = null,

    @CreatedDate
    var createdAt: Instant = Instant.now()
) {
}