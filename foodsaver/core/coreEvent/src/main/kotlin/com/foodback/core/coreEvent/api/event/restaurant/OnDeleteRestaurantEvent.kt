package com.foodback.core.coreEvent.api.event.restaurant

import com.foodback.core.coreEvent.api.event.BaseApplicationEvent
import java.util.UUID

class OnDeleteRestaurantEvent(restaurantId: UUID)
    : BaseApplicationEvent(aggregateId = restaurantId)