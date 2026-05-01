package com.foodback.base.module.api.dto

import java.util.UUID

data class CompanyResponse(
    val id: UUID,
    val companyName: String,
    val companyDescription: String?,
    val logoUri: String?,

)
