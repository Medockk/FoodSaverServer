package com.foodback.app.enterprises.mapper

import com.foodback.app.common.dto.response.OrganizationResponseV1
import com.foodback.app.enterprises.dto.response.EnterpriseResponseV1
import com.foodback.app.enterprises.entity.EnterprisesEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class EnterpriseMappersV1(
    @Value($$"${server.protocol}")
    private val protocol: String,
    @Value($$"${server.address}")
    private val address: String,
    @Value($$"${server.port}")
    private val port: String,
) {

    private val url = "$protocol://$address:$port/"

    fun mapToResponse(enterprisesEntity: EnterprisesEntity) = with(enterprisesEntity) {
        EnterpriseResponseV1(
            id = id!!,
            latitude = latitude,
            longitude = longitude,
            addressName = addressName,
            organization = organization!!.let {
                OrganizationResponseV1(
                    id = it.id!!,
                    organizationName = it.organizationName,
                    owner = it.owner,
                    createdAt = it.createdAt!!
                )
            }
        )
    }

    fun mapImageUrls(imageUrls: List<String>): List<String> {
        return imageUrls.map { imageUrl ->
            if (imageUrl.startsWith("http")) {
                imageUrl
            } else {
                if (imageUrl.startsWith("/")) {
                    url + imageUrl.drop(1)
                } else {
                    url + imageUrl
                }
            }
        }
    }

    fun mapRelativeUrlToAbsoluteUrl(relativeUrl: String): String {
        return if (relativeUrl.startsWith("http")) {
            relativeUrl
        } else if (relativeUrl.startsWith("/")) {
            url + relativeUrl.drop(1)
        } else {
            url + relativeUrl
        }
    }
}