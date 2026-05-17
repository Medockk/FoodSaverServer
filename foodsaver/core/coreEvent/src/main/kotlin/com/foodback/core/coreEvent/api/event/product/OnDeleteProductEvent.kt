package com.foodback.core.coreEvent.api.event.product

import com.foodback.core.coreEvent.api.event.BaseApplicationEvent
import java.util.UUID

class OnDeleteProductEvent(productId: UUID)
    : BaseApplicationEvent(aggregateId = productId)