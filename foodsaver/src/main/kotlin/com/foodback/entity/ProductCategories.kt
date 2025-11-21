package com.foodback.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.*

/**
 * Special db entity with name products_categories
 * @param id Identifier of current category
 * @param name Name of current category
 */
@Entity
@Table(name = "products_categories")
data class ProductCategories(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,
    var name: String = ""
)
