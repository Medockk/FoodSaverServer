package com.foodback.entity

import com.foodback.entity.User.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "payment_methods")
data class PaymentMethodEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    var user: UserEntity? = null,

    @Column(nullable = false)
    var bank: String = "",

    @Column(nullable = false)
    var cardNumber: String = "",

    @Column(nullable = false)
    var isSelected: Boolean = false,
) {
    override fun toString(): String {
        return "${this::class.java.simpleName}(" +
                "id = $id," +
                "user = ${user?.uid}," +
                "bank = $bank," +
                "cardNumber = $cardNumber," +
                "isSelected = $isSelected," +
                ")"
    }
}
