package com.foodback.app.common.dto.response

import java.time.Instant
import java.util.*

data class OrganizationResponseV1(
    var id: UUID,
    var organizationName: String,
    var owner: String,
    var createdAt: Instant
)
