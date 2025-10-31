package com.foodback.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = "",

    var title: String,
    var description: String,

    var cost: Long,

    var rating: Long,
)
