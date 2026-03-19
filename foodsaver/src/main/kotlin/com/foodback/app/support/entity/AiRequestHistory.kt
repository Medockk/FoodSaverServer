package com.foodback.app.support.entity

import com.foodback.app.user.entity.UserEntity
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
@Table(name = "ai_request_history")
class AiRequestHistory(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    var user: UserEntity? = null,

    @Column(nullable = false)
    var message: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiRequestHistory) return false
        return id != null && other.id == id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return """
            ${this::class.java.simpleName}(
                id = $id,
                user = ${user?.uid},
                message = $message
            )
        """.trimIndent()
    }
}