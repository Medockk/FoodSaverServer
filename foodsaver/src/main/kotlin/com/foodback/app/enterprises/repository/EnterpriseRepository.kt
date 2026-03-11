package com.foodback.app.enterprises.repository

import com.foodback.app.enterprises.entity.EnterprisesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface EnterpriseRepository: JpaRepository<EnterprisesEntity, UUID> {

    @Query("""
        SELECT * FROM enterprises p
        ORDER BY p.coords <-> ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)
        LIMIT 7
    """, nativeQuery = true)
    fun findNearestEnterprises(
        @Param("lat")
        latitude: Double,
        @Param("lon")
        longitude: Double
    ): List<EnterprisesEntity>
}