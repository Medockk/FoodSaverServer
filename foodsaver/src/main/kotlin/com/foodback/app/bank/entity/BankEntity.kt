package com.foodback.app.bank.entity

import com.foodback.app.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "foodsaver_bank")
data class BankEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false)
    var balance: Double = 0.0,

    @Column(nullable = false)
    var cardNumber: String = "",

    @Column(nullable = false)
    var isSelected: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    var user: UserEntity? = null
) {
    override fun toString(): String {
        return """
            ${this::class.java.simpleName}(
                id = $id,
                balance = $balance,
                cardNumber = $cardNumber,
                isSelected = $isSelected,
                user.uid = ${user?.uid}
            )
        """.trimIndent()
    }
}