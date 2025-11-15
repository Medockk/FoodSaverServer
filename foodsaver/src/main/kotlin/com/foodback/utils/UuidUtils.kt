package com.foodback.utils

import com.foodback.exception.general.UuidException
import java.util.UUID

/**
 * Special Util function to convert [String] value to [UUID] value
 * @throws UuidException if [String] failed to parse to [UUID]
 * @return [UUID] if [String] successful converted
 */
@Throws(UuidException::class)
fun String.toUUID(): UUID {
    return try {
        UUID.fromString(this)
    } catch (e: Exception) {
        e.printStackTrace()
        throw UuidException()
    }
}