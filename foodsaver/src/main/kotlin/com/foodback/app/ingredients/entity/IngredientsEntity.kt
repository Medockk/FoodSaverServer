package com.foodback.app.ingredients.entity

import com.foodback.app.product.entity.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "ingredients")
class IngredientsEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var product: ProductEntity? = null,

    var ingredients: MutableList<String> = mutableListOf()
) {
    override fun toString(): String {
        return """
            ${this::class.java.simpleName}(
                id = $id,
                product = ${product?.id},
                ingredients = $ingredients
            )
        """.trimIndent()
    }
}