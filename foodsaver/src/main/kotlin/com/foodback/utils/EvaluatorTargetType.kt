package com.foodback.utils

/**
 * Special ENUM-class for check special permissions type
 */
enum class EvaluatorTargetType(val type: String) {

    PRODUCT_TYPE_DELETE(type = "PRODUCT_TYPE_DELETE"),
    PRODUCT_TYPE_UPDATE(type = "PRODUCT_TYPE_UPDATE"),
    PRODUCT_TYPE_ADD(type = "PRODUCT_TYPE_ADD"),
}