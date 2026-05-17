package com.foodback.core.coreCommon.api.search

import java.util.UUID

interface SearchProductProvider {

    fun findProductIds(query: String): List<UUID>
}