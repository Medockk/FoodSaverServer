package com.foodback.app.cart.entity

import com.foodback.app.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

/**
 * Special entity to save User cart to database
 * @param id unique identifier of current cart
 * @param uid User identifier
 * @param productCount Count of products, saved in cart
 * @param items A [CartItemEntity] - special related-table entity
 */
@Entity
@Table(name = "cart")
data class CartEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", referencedColumnName = "uid", nullable = false)
    var user: UserEntity? = null,

    @Column(nullable = false)
    var productCount: Long = 0,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "cart",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var items: MutableList<CartItemEntity> = mutableListOf()
)