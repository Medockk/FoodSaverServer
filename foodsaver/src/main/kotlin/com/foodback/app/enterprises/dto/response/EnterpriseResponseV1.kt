package com.foodback.app.enterprises.dto.response

import com.foodback.app.common.dto.response.OrganizationResponseV1
import java.util.*

data class EnterpriseResponseV1(
    val id: UUID,
    val latitude: Double,
    val longitude: Double,
    val addressName: String,

    val organization: OrganizationResponseV1
)
