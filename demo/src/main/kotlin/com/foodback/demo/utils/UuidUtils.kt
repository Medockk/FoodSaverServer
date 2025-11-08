package com.foodback.demo.utils

import com.foodback.demo.exception.general.UuidException
import java.util.*

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