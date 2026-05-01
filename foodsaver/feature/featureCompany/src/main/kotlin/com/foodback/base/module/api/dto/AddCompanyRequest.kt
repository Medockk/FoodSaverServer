package com.foodback.base.module.api.dto

data class AddCompanyRequest(
    val companyName: String,
    val companyDescription: String?,
    val ownerFullName: String
)
