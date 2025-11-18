package com.foodback.security.authority

import com.foodback.entity.User.Permissions
import com.foodback.utils.EvaluatorTargetType
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component("haveAuthority")
class AuthoritySecurityFunctions(
    private val permissionEvaluator: PermissionEvaluator
) {

    fun canDeleteProduct(productId: UUID, authentication: Authentication): Boolean {
        return permissionEvaluator.hasPermission(
            authentication, productId, EvaluatorTargetType.PRODUCT_TYPE.type,
            Permissions.DELETE_PRODUCT.value
        )
    }
}