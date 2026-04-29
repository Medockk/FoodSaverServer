package com.foodback.app.user.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_location")
class UserLocationEntity(
    @Id
    var id: UUID? = null,

    @Column(nullable = false)
    var latitude: Double? = null,
    @Column(nullable = false)
    var longitude: Double? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    var user: UserEntity? = null
) {
    override fun toString(): String {
        return """
            ${this::class.java.simpleName}(
                id = $id,
                latitude = $latitude,
                longitude = $longitude,
                user = ${user?.uid}
            )
        """.trimIndent()
    }
}