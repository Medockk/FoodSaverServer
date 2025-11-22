package com.foodback.security.authority

import com.foodback.entity.User.Permissions
import com.foodback.utils.EvaluatorTargetType
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

/**
 * Special class to appeal in SpEL-expression
 */
@Component("haveAuthority")
class AuthoritySecurityFunctions(
    private val permissionEvaluator: PermissionEvaluator
) {

    /**
     * Method to check: can user delete the product or not
     */
    fun canDeleteProduct(productId: UUID, authentication: Authentication): Boolean {
        return permissionEvaluator.hasPermission(
            authentication, productId, EvaluatorTargetType.PRODUCT_TYPE_DELETE.type,
            Permissions.DELETE_PRODUCT.value
        )
    }

    /**
     * Method to check: can user add the product or not
     */
    fun canAddProduct(authentication: Authentication): Boolean {
        return permissionEvaluator.hasPermission(
            authentication, "", EvaluatorTargetType.PRODUCT_TYPE_ADD.type,
            Permissions.ADD_PRODUCT.value
        )
    }

    /**
     * Method to check: can user update the product or not
     */
    fun canUpdateProduct(productId: UUID, authentication: Authentication): Boolean {
        return permissionEvaluator.hasPermission(
            authentication, productId, EvaluatorTargetType.PRODUCT_TYPE_UPDATE.type,
            Permissions.UPDATE_PRODUCT.value
        )
    }
}