package com.foodback.demo.dto.request.mappers

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.entity.CartEntity

fun CartRequestModel.toEntity(
    uid: String
) =
    CartEntity(
        uid = uid,
        items = mutableListOf()
    )