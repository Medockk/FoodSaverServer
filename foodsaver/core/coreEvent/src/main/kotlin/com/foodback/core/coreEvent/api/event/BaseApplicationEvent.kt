package com.foodback.core.coreEvent.api.event

import java.time.Instant
import java.util.UUID

abstract class BaseApplicationEvent(
    open val aggregateId: Any,
    open val traceId: UUID = UUID.randomUUID(),
    open val timestamp: Instant = Instant.now()
)